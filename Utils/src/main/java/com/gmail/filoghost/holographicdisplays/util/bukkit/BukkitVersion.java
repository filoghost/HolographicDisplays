package com.gmail.filoghost.holographicdisplays.util.bukkit;

import java.util.Optional;

/**
 * An enumerator representing the Bukkit server version.
 */
public enum BukkitVersion {
	UNKNOWN,
	v1_7_R1,
	v1_7_R2,
	v1_7_R3,
	v1_7_R4,
	v1_8_R1,
	v1_8_R2,
	v1_8_R3,
	v1_9_R1,
	v1_9_R2,
	v1_10_R1,
	v1_11_R1,
	v1_12_R1;

	private static BukkitVersion currentVersion;

	static {
		currentVersion = Optional.ofNullable(BukkitUtils.getNMSVersion())
				.map(BukkitVersion::valueOf)
				.orElse(UNKNOWN);
	}

	public static BukkitVersion getCurrentVersion() {
		return currentVersion;
	}

	public static boolean isAtLeast(BukkitVersion other) {
		return getCurrentVersion().ordinal() >= other.ordinal();
	}

	public static boolean isBetweenOrEqual(BukkitVersion first, BukkitVersion second) {
		return isAtLeast(first) && getCurrentVersion().ordinal() <= second.ordinal();
	}
}
