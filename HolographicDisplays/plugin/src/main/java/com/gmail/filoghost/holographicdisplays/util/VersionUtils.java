package com.gmail.filoghost.holographicdisplays.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class VersionUtils {
	
	private static Method getOnlinePlayersMethod;
	private static boolean getOnlinePlayersUseReflection;
	
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
		if (!MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
			return false;
		}
		
		return type == EntityType.ARMOR_STAND;
	}
	
	
	public static Collection<? extends Player> getOnlinePlayers() {
		try {
			
			if (getOnlinePlayersMethod == null) {
				getOnlinePlayersMethod = Bukkit.class.getDeclaredMethod("getOnlinePlayers");
				if (getOnlinePlayersMethod.getReturnType() == Player[].class) {
					getOnlinePlayersUseReflection = true;
				}
			}
		
			if (!getOnlinePlayersUseReflection) {
				return Bukkit.getOnlinePlayers();
			} else {
				Player[] playersArray = (Player[]) getOnlinePlayersMethod.invoke(null);
				return ImmutableList.copyOf(playersArray);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	
	/**
	 * @return 1 if reference > comparison, 0 if reference == comparison, -1 if reference < comparison
	 */
	private static int compare(String reference, String comparison) throws NumberFormatException {
		String[] referenceSplit = reference.split("\\.");
		String[] comparisonSplit = comparison.split("\\.");
		
		int longest = Math.max(referenceSplit.length, comparisonSplit.length);
		
		// Default value is 0
		int[] referenceNumbersArray = new int[longest];
		int[] comparisonNumbersArray = new int[longest];
		
		for (int i = 0; i < referenceSplit.length; i++) {
			referenceNumbersArray[i] = Integer.parseInt(referenceSplit[i]);
		}
		
		for (int i = 0; i < comparisonSplit.length; i++) {
			comparisonNumbersArray[i] = Integer.parseInt(comparisonSplit[i]);
		}
		
		for (int i = 0; i < longest; i++) {
			int diff = referenceNumbersArray[i] - comparisonNumbersArray[i];
			if (diff > 0) {
				return 1;
			} else if (diff < 0) {
				return -1;
			}
		}
		
		return 0;
	}
	
	
	public static boolean isVersionGreaterEqual(String reference, String thanWhat) {
		return compare(reference, thanWhat) >= 0;
	}
	
	public static boolean isVersionLessEqual(String reference, String thanWhat) {
		return compare(reference, thanWhat) <= 0;
	}
	
	public static boolean isVersionBetweenEqual(String reference, String lowest, String highest) {
		return isVersionGreaterEqual(reference, lowest) && isVersionLessEqual(reference, highest);
	}
	

	public static boolean classExists(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
	
}
