package com.gmail.filoghost.holographicdisplays.util;

/*
 * Since 1.8 we use armor stands instead of wither skulls.
 * Since 1.9 there is a different offset for the nametag.
 * Since 1.10 there is a difference in the entity metadata packet index for items.
 */
public class MinecraftVersion {
	
	private static MinecraftVersion version;

	public static final MinecraftVersion
	
		v1_7 = new MinecraftVersion(1),
		
		// Since 1.8 we use armor stands instead of wither skulls.
		v1_8 = new MinecraftVersion(2),
		
		// Since 1.9 there is a different offset for the nametag.
		v1_9 = new MinecraftVersion(3),
		
		// Since 1.10 there is a difference in the entity metadata packet index for items.
		v1_10 = new MinecraftVersion(4),
	
		v1_11 = new MinecraftVersion(5),
		
		v1_12 = new MinecraftVersion(6);

	private int value;
	
	private MinecraftVersion(int value) {
		this.value = value;
	}
	
	public static void set(MinecraftVersion version) {
		if (version == null) {
			throw new NullPointerException("version");
		}
		if (MinecraftVersion.version != null) {
			throw new IllegalArgumentException("version already set");
		}
		MinecraftVersion.version = version;
	}
	
	public static MinecraftVersion get() {
		return version;
	}
	
	public static boolean isGreaterEqualThan(MinecraftVersion other) {
		return MinecraftVersion.version.value >= other.value;
	}
	
}
