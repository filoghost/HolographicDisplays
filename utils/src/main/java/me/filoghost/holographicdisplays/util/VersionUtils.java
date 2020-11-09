/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.holographicdisplays.util;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtils {
	
	private static final boolean IS_PAPER_SERVER = Bukkit.getName().equals("Paper");
	
	
	/**
	 * Paper contains some code changes compared to Spigot.
	 */
	public static boolean isPaperServer() {
		return IS_PAPER_SERVER;
	}
	
	
	/**
	 * This method uses a regex to get the NMS package part that changes with every update.
	 * Example: v1_13_R2
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
