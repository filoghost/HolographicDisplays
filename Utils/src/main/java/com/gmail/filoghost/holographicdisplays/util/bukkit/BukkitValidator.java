package com.gmail.filoghost.holographicdisplays.util.bukkit;

import com.gmail.filoghost.holographicdisplays.util.Validator;
import org.bukkit.Bukkit;

/**
 * Bukkit validation utils.
 */
public final class BukkitValidator {

	private BukkitValidator() {
	}

	public static void isSync(String message) {
		Validator.isTrue(Bukkit.isPrimaryThread(), message);
	}
}
