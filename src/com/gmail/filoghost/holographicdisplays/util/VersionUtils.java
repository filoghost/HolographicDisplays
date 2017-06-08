package com.gmail.filoghost.holographicdisplays.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.google.common.collect.ImmutableList;

public class VersionUtils {
	
	private static Method oldGetOnlinePlayersMethod;
	
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
		if (!HolographicDisplays.is18orGreater()) {
			return false;
		}
		
		return type == EntityType.ARMOR_STAND;
	}
	
	
	public static Collection<? extends Player> getOnlinePlayers() {
		if (HolographicDisplays.is18orGreater()) {
			return Bukkit.getOnlinePlayers();
		} else {
			try {
				if (oldGetOnlinePlayersMethod == null) {
					oldGetOnlinePlayersMethod = Bukkit.class.getDeclaredMethod("getOnlinePlayers");
				}
				
				Player[] playersArray = (Player[]) oldGetOnlinePlayersMethod.invoke(null);
				return ImmutableList.copyOf(playersArray);
			} catch (Exception e) {
				e.printStackTrace();
				return Collections.emptyList();
			}
		}
	}
}
