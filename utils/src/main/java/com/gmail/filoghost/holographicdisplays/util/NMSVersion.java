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
 * The NMS version is the name of the main package under net.minecraft.server.
 */
public enum NMSVersion {
	
	v1_8_R2,
	v1_8_R3,
	v1_9_R1,
	v1_9_R2,
	v1_10_R1,
	v1_11_R1,
	v1_12_R1,
	v1_13_R1,
	v1_13_R2,
	v1_14_R1,
	v1_15_R1,
	v1_16_R1,
	v1_16_R2,
	v1_16_R3;
	
	private static final NMSVersion CURRENT_VERSION = extractCurrentVersion();
	
	
	private static NMSVersion extractCurrentVersion() {
		String nmsVersionName = VersionUtils.extractNMSVersion();
		
		if (nmsVersionName != null) {
			try {
				return valueOf(nmsVersionName);
			} catch (IllegalArgumentException e) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	
	public static boolean isValid() {
		return CURRENT_VERSION != null;
	}

	
	public static NMSVersion getCurrent() {
		if (CURRENT_VERSION == null) {
			throw new IllegalStateException("Current version not set");
		}
		return CURRENT_VERSION;
	}
	
	
	public static boolean isGreaterEqualThan(NMSVersion other) {
		return getCurrent().ordinal() >= other.ordinal();
	}
	
	
	public static boolean isBetween(NMSVersion from, NMSVersion to) {
		return from.ordinal() <= getCurrent().ordinal() && getCurrent().ordinal() <= to.ordinal();
	}
	
	
}
