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
