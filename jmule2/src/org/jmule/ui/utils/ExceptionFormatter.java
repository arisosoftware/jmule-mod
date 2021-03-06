/*
 *  JMule - Java file sharing client
 *  Copyright (C) 2007-2008 JMule team ( jmule@jmule.org / http://jmule.org )
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
package org.jmule.ui.utils;

/**
 * Created on Aug 23, 2008
 * 
 * @author binary256
 * @version $Revision: 1.1 $ Last changed by $Author: binary256_ $ on $Date:
 *          2008/08/26 19:38:33 $
 */
public class ExceptionFormatter {

	public static String getExceptionMessage(Throwable e) {
		String message = e.getMessage();
		if (message == null || message.length() == 0) {
			message = e.getClass().getName();
			int pos = message.lastIndexOf(".");
			message = message.substring(pos + 1);
		} else if (e instanceof ClassNotFoundException) {
			if (message.toLowerCase().indexOf("found") == -1) {
				message = "Class " + message + " not found";
			}
		}
		return (message);
	}

}
