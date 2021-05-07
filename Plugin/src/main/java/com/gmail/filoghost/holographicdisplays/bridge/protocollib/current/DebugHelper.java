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

import java.util.Map;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.EquivalentConverter;
import com.comphenix.protocol.reflect.PrettyPrinter;
import com.comphenix.protocol.reflect.PrettyPrinter.ObjectPrinter;
import com.comphenix.protocol.utility.HexDumper;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BukkitConverters;

public class DebugHelper {
	
	private static final int HEX_DUMP_THRESHOLD = 256;
	
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
			System.out.println(shortDescription + ":\n" + getPacketDescription(event.getPacket()));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("Unable to use reflection.");
		}
	}
	
	
	private static String getPacketDescription(PacketContainer packetContainer) throws IllegalAccessException {
		Object packet = packetContainer.getHandle();
		Class<?> clazz = packet.getClass();
		
		// Get the first Minecraft super class
		while (clazz != null && clazz != Object.class &&
				(!MinecraftReflection.isMinecraftClass(clazz))) {
			clazz = clazz.getSuperclass();
		}

		return PrettyPrinter.printObject(packet, clazz, MinecraftReflection.getPacketClass(), PrettyPrinter.RECURSE_DEPTH, new ObjectPrinter() {
			@Override
			public boolean print(StringBuilder output, Object value) {
				// Special case
				if (value instanceof byte[]) {
					byte[] data = (byte[]) value;
					
					if (data.length > HEX_DUMP_THRESHOLD) {
						output.append("[");
						HexDumper.defaultDumper().appendTo(output, data);
						output.append("]");
						return true;
					}
				} else if (value != null) {
					EquivalentConverter<Object> converter = findConverter(value.getClass());

					if (converter != null) {
						output.append(converter.getSpecific(value));
						return true;
					}
				}
				return false;
			}
		});
	}
	
	
	private static EquivalentConverter<Object> findConverter(Class<?> clazz) {
		Map<Class<?>, EquivalentConverter<Object>> converters = BukkitConverters.getConvertersForGeneric();
		
		while (clazz != null) {
			EquivalentConverter<Object> result = converters.get(clazz);
			
			if (result != null)
				return result;
			else
				clazz = clazz.getSuperclass();
		}
		return null;
	}

}
