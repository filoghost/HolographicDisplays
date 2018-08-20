package com.gmail.filoghost.holographicdisplays.util;

/**
 *  When spawning a hologram at a location, the top part of the first line should be exactly on that location.
 *  The second line is below the first, and so on.
 */
public class Offsets {
	
	public static final double
		
		// For 1.8+, a single armor stand.
		ARMOR_STAND_ALONE = -0.26,
		
		// For 1.8+, an armor stand holding an item.
		ARMOR_STAND_WITH_ITEM = -1.48,
	
		// For 1.8+, an armor stand holding a slime.
		ARMOR_STAND_WITH_SLIME = -1.49,
	
	
		// For 1.9+, a single armor stand.
		ARMOR_STAND_ALONE_1_9 = -0.29,
		
		// For 1.9+, an armor stand holding an item.
		ARMOR_STAND_WITH_ITEM_1_9 = -0.0,
	
		// For 1.9+, an armor stand holding a slime.
		ARMOR_STAND_WITH_SLIME_1_9 = -0.01;

}
