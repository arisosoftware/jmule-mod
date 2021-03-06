package org.jmule.core.bccrypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import static org.jmule.core.bccrypto.ASN1.*;
import static org.jmule.core.bccrypto.DER.*;

public class JCERSAPublicKey implements RSAPublicKey {
	private BigInteger modulus;
	private BigInteger publicExponent;

	JCERSAPublicKey(RSAKeyParameters key) {
		this.modulus = key.getModulus();
		this.publicExponent = key.getExponent();
	}

	JCERSAPublicKey(RSAPublicKeySpec spec) {
		this.modulus = spec.getModulus();
		this.publicExponent = spec.getPublicExponent();
	}

	JCERSAPublicKey(RSAPublicKey key) {
		this.modulus = key.getModulus();
		this.publicExponent = key.getPublicExponent();
	}

	JCERSAPublicKey(SubjectPublicKeyInfo info) {
		try {
			RSAPublicKeyStructure pubKey = new RSAPublicKeyStructure((ASN1Sequence) info.getPublicKey());

			this.modulus = pubKey.getModulus();
			this.publicExponent = pubKey.getPublicExponent();
		} catch (IOException e) {
			throw new IllegalArgumentException("invalid info structure in RSA public key");
		}
	}

	/**
	 * return the modulus.
	 *
	 * @return the modulus.
	 */
	public BigInteger getModulus() {
		return modulus;
	}

	/**
	 * return the public exponent.
	 *
	 * @return the public exponent.
	 */
	public BigInteger getPublicExponent() {
		return publicExponent;
	}

	public String getAlgorithm() {
		return "RSA";
	}

	public String getFormat() {
		return "X.509";
	}

	public byte[] getEncoded() {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DEROutputStream dOut = new DEROutputStream(bOut);
		SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(
				new AlgorithmIdentifier(Identifiers.rsaEncryption, new DERNull()),
				new RSAPublicKeyStructure(getModulus(), getPublicExponent()).getDERObject());

		try {
			dOut.writeObject(info);
			dOut.close();
		} catch (IOException e) {
			throw new RuntimeException("Error encoding RSA public key");
		}

		return bOut.toByteArray();

	}

	public boolean equals(Object o) {
		if (!(o instanceof RSAPublicKey)) {
			return false;
		}

		if (o == this) {
			return true;
		}

		RSAPublicKey key = (RSAPublicKey) o;

		return getModulus().equals(key.getModulus()) && getPublicExponent().equals(key.getPublicExponent());
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		String nl = System.getProperty("line.separator");

		buf.append("RSA Public Key" + nl);
		buf.append("            modulus: " + this.getModulus().toString(16) + nl);
		buf.append("    public exponent: " + this.getPublicExponent().toString(16) + nl);

		return buf.toString();
	}
}
