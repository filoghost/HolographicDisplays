package com.gmail.filoghost.holographicdisplays.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public class VersionUtils {
	
	private static final boolean IS_PAPER_SERVER = Bukkit.getName().equals("Paper");
	
	
	/**
	 * Paper contains some changes that require
	 */
	public static boolean isPaperServer() {
		return IS_PAPER_SERVER;
	}
	
	
	/**
	 * This method uses a regex to get the NMS package part that changes with every update.
	 * Example: v1_8_R1
	 * @return the NMS package part or null if not found.
	 */
	public static String extractNMSVersion() {
		Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
		if (matcher.find()) {
			return matcher.group();
		} else {
			return null;
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
	
}
