/*
 *  JMule - Java file sharing client
 *  Copyright (C) 2007-2009 JMule Team ( jmule@jmule.org / http://jmule.org )
 *
 *  Any parts of this program derived from other projects, or contributed
 *  by third-party developers are copyrighted by their respective authors.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package org.jmule.core.jkad.search;

import java.util.List;

import org.jmule.core.jkad.ContactAddress;
import org.jmule.core.jkad.Int128;
import org.jmule.core.jkad.JKadConstants;
import org.jmule.core.jkad.JKadConstants.RequestType;
import org.jmule.core.jkad.JKadException;
import org.jmule.core.jkad.lookup.Lookup;
import org.jmule.core.jkad.lookup.LookupTask;
import org.jmule.core.jkad.packet.KadPacket;
import org.jmule.core.jkad.packet.PacketFactory;
import org.jmule.core.jkad.routingtable.KadContact;

/**
 * Created on Jan 16, 2009
 * 
 * @author binary256
 * @version $Revision: 1.15 $ Last changed by $Author: binary255 $ on $Date:
 *          2010/10/23 05:51:49 $
 */
public class NoteSearchTask extends SearchTask {

	private LookupTask lookup_task = null;
	private long fileSize;

	public NoteSearchTask(Int128 searchID, long fileSize) {
		super(searchID);
		this.fileSize = fileSize;

	}

	public void start() throws JKadException {
		isStarted = true;

		lookup_task = new LookupTask(RequestType.FIND_VALUE, searchID, JKadConstants.LOOKUP_SEARCH_NOTE_TIMEOUT) {
			public void lookupTimeout() {
			}

			public void processToleranceContacts(ContactAddress sender, List<KadContact> results) {

				for (KadContact contact : results) {
					KadPacket responsePacket = null;
					if (contact.supportKad2())
						responsePacket = PacketFactory.getNotes2Req(searchID, fileSize);
					else
						responsePacket = PacketFactory.getNotes1Req(searchID);
					_network_manager.sendKadPacket(responsePacket, contact.getContactAddress().getAddress(),
							contact.getUDPPort());

					 
				}
			}

			public void lookupTerminated() {
				stop();
			}

		};
		Lookup.getSingleton().addLookupTask(lookup_task);
		if (listener != null)
			listener.searchStarted();
	}

	public void stop() {
		if (!isStarted)
			return;
		isStarted = false;
		// ((InternalJKadManager)JKadManagerSingleton.getInstance()).removePacketListener(getPacketListenerList());
		if (listener != null)
			listener.searchFinished();
		Search.getSingleton().cancelSearch(searchID);
	}

	public void stopSearchRequest() {
		if (!isStarted)
			return;
		Lookup.getSingleton().removeLookupTask(searchID);
	}

}
