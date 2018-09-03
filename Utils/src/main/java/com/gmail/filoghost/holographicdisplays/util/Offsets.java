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
package com.gmail.filoghost.holographicdisplays.util;

/**
 *  When spawning a hologram at a location, the top part of the first line should be exactly on that location.
 *  The second line is below the first, and so on.
 */
public class Offsets {
	
	public static final double
		
		// A single armor stand.
		ARMOR_STAND_ALONE = getArmorStandAloneOffset(),
		
		// An armor stand holding an item.
		ARMOR_STAND_WITH_ITEM = getArmorStandWithItemOffset(),
	
		// An armor stand holding a slime.
		ARMOR_STAND_WITH_SLIME = getArmorStandWithSlimeOffset();
	
	
	private static double getArmorStandAloneOffset() {
		if (NMSVersion.getCurrent() == NMSVersion.v1_8_R1) {
			// When the NBT tag "Marker" was not implemented
			return -1.25;
		} else {
			return -0.29;
		}
	}
	
	
	private static double getArmorStandWithItemOffset() {
		if (NMSVersion.getCurrent() == NMSVersion.v1_8_R1) {
			// When the NBT tag "Marker" was not implemented
			return -1.48;
		} else {
			return 0;
		}
	}
	
	
	private static double getArmorStandWithSlimeOffset() {
		if (NMSVersion.getCurrent() == NMSVersion.v1_8_R1) {
			// When the NBT tag "Marker" was not implemented
			return -1.48;
		} else {
			return 0;
		}
	}
	

}
