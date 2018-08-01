package com.gmail.filoghost.holographicdisplays.constant;

/**
 * A class containing the constant hologram spacing offsets values.
 */
public final class Offsets {

	private Offsets() {
	}

	// For 1.8+, a single armor stand. As with wither skulls and horses, the bottom part of the nametag should be on the surface of the block.
	public static final double ARMOR_STAND_ALONE = -1.25;
	// For 1.8+, an armor stand holding an item.
	public static final double ARMOR_STAND_WITH_ITEM = -1.48;
	// For 1.8+, an armor stand holding a slime.
	public static final double ARMOR_STAND_WITH_SLIME = -1.49;

	// For 1.9+, a single armor stand. As with wither skulls and horses, the bottom part of the nametag should be on the surface of the block.
	public static final double ARMOR_STAND_ALONE_1_9 = -0.29;
	// For 1.9+, an armor stand holding an item.
	public static final double ARMOR_STAND_WITH_ITEM_1_9 = -0.0;
	// For 1.9+, an armor stand holding a slime.
	public static final double ARMOR_STAND_WITH_SLIME_1_9 = -0.01;
}
