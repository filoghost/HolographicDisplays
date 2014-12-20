package com.gmail.filoghost.holographicdisplays.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;

public class VersionUtils {
	
	/**
	 * This method uses a regex to get the NMS package part that changes with every update.
	 * Example: v1_8_R1
	 * @return the NMS package part or null if not found.
	 */
	public static String getBukkitVersion() {
		Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
		if (matcher.find()) {
			return matcher.group();
		} else {
			return null;
		}
	}
	
	/**
	 * This method uses a regex to get the version of this Minecraft release.
	 * Example: 1.8.1
	 * @return the version of this release or null if not found.
	 */
	public static String getMinecraftVersion() {
		Matcher matcher = Pattern.compile("(\\(MC: )([\\d\\.]+)(\\))").matcher(Bukkit.getVersion());
		if (matcher.find()) {
			return matcher.group(2);
		} else {
			return null;
		}
	}
	
	/**
	 * Checks if the server is using MCPC+ or Cauldron.
	 * @return true if the server software is MCPC+ or Cauldron
	 */
	public static boolean isMCPCOrCauldron() {
		return Utils.containsIgnoreCase(Bukkit.getName(), "MCPC") || Utils.containsIgnoreCase(Bukkit.getName(), "Cauldron");
	}
	
	public static boolean isArmorstand(EntityType type) {
		if (!HolographicDisplays.is1_8()) {
			return false;
		}
		
		return type == EntityType.ARMOR_STAND;
	}
}
