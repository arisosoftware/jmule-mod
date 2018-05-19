package org.jmule.core.bccrypto;

import java.util.Enumeration;

import static org.jmule.core.bccrypto.ASN1.*;

/**
 * The DigestInfo object.
 * 
 * <pre>
 * DigestInfo::=SEQUENCE{
 *          digestAlgorithm  AlgorithmIdentifier,
 *          digest OCTET STRING }
 * </pre>
 */
public class DigestInfo extends ASN1Encodable {
	private byte[] digest;
	private AlgorithmIdentifier algId;

	public static DigestInfo getInstance(ASN1TaggedObject obj, boolean explicit) {
		return getInstance(ASN1Sequence.getInstance(obj, explicit));
	}

	public static DigestInfo getInstance(Object obj) {
		if (obj instanceof DigestInfo) {
			return (DigestInfo) obj;
		} else if (obj instanceof ASN1Sequence) {
			return new DigestInfo((ASN1Sequence) obj);
		}

		throw new IllegalArgumentException("unknown object in factory");
	}

	public DigestInfo(AlgorithmIdentifier algId, byte[] digest) {
		this.digest = digest;
		this.algId = algId;
	}

	public DigestInfo(ASN1Sequence obj) {
		Enumeration e = obj.getObjects();

		algId = AlgorithmIdentifier.getInstance(e.nextElement());
		digest = ASN1OctetString.getInstance(e.nextElement()).getOctets();
	}

	public AlgorithmIdentifier getAlgorithmId() {
		return algId;
	}

	public byte[] getDigest() {
		return digest;
	}

	public DER.DERObject toASN1Object() {
		ASN1EncodableVector v = new ASN1EncodableVector();

		v.add(algId);
		v.add(new DER.DEROctetString(digest));

		return new DER.DERSequence(v);

	}
}
