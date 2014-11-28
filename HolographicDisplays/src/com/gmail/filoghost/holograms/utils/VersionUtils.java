package com.gmail.filoghost.holograms.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import com.gmail.filoghost.holograms.HolographicDisplays;

public class VersionUtils {
	
	public static String getBukkitVersion() {
		Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
		if (matcher.find()) {
			return matcher.group();
		} else {
			return null;
		}
	}
	
	public static String getMinecraftVersion() {
		Matcher matcher = Pattern.compile("(\\(MC: )([\\d\\.]+)(\\))").matcher(Bukkit.getVersion());
		if (matcher.find()) {
			return matcher.group(2);
		} else {
			return null;
		}
	}
	
	public static boolean isMCPCOrCauldron() {
		// This returns the server software e.g. CraftBukkit, Cauldron-MCPC-Plus, ...
		return Bukkit.getName().toLowerCase().contains("mcpc") || Bukkit.getName().toLowerCase().contains("cauldron");
	}
	
	public static boolean isArmorstand(EntityType type) {
		if (!HolographicDisplays.is1_8) {
			return false;
		}
		
		return type == EntityType.ARMOR_STAND;
	}
}
