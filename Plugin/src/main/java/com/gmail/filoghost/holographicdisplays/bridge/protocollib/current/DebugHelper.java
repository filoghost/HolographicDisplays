/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.bridge.protocollib.current;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.HexDumper;

public class DebugHelper {
	
	public static void printInformation(PacketEvent event) {
		String verb = event.isServerPacket() ? "Sent" : "Received";
		String format = event.isServerPacket() ? 
				"%s %s to %s" : 
				"%s %s from %s";
		
		String shortDescription = String.format(format,
				event.isCancelled() ? "Cancelled" : verb,
				event.getPacketType(),
				event.getPlayer().getName()
		);
		
		// Detailed will print the packet's content too
		try {			
			System.out.println(shortDescription + ":\n" + HexDumper.getPacketDescription(event.getPacket()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("Unable to use reflection.");
		}
	}
	
}
