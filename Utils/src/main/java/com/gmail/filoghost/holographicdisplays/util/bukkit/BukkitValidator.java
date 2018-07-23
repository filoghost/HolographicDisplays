package com.gmail.filoghost.holographicdisplays.util.bukkit;

import com.gmail.filoghost.holographicdisplays.util.Validator;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * Bukkit validation utils.
 */
@UtilityClass
public class BukkitValidator {

	public static void isSync(String message) {
		Validator.isTrue(Bukkit.isPrimaryThread(), message);
	}
}
