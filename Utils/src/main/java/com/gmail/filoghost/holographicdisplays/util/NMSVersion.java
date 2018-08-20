package com.gmail.filoghost.holographicdisplays.util;

/**
 * The NMS version is the name of the main package under net.minecraft.server.
 */
public enum NMSVersion {
	
	v1_8_R1,
	v1_8_R2,
	v1_8_R3,
	v1_9_R1,
	v1_9_R2,
	v1_10_R1,
	v1_11_R1,
	v1_12_R1,
	v1_13_R1;
	
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
