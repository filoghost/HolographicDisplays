package com.gmail.filoghost.holographicdisplays.util.bukkit;

import lombok.Getter;

import java.util.Optional;

/**
 * An enumerator representing the Bukkit server version.
 */
public enum BukkitVersion {
	UNKNOWN,
	V1_7_R1,
	V1_7_R2,
	V1_7_R3,
	V1_7_R4,
	V1_8_R1,
	V1_8_R2,
	V1_8_R3,
	V1_9_R1,
	V1_9_R2,
	V1_10_R1,
	V1_11_R1,
	V1_12_R1;

	@Getter
	private static BukkitVersion currentVersion;

	static {
		currentVersion = Optional.ofNullable(BukkitUtils.getNMSVersion())
				.map(BukkitVersion::valueOf)
				.orElse(UNKNOWN);
	}

	public static boolean isAtLeast(BukkitVersion other) {
		return getCurrentVersion().ordinal() >= other.ordinal();
	}

	public static boolean isBetweenOrEqual(BukkitVersion first, BukkitVersion second) {
		return isAtLeast(first) && getCurrentVersion().ordinal() <= second.ordinal();
	}
}
