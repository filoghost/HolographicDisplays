package com.gmail.filoghost.holographicdisplays.disk;

import org.bukkit.ChatColor;

public class StringConverter {
	
	public static String toReadableFormat(String input) {
		if (input == null) {
			return null;
		}

		input = UnicodeSymbols.placeholdersToSymbols(input);
		input = ChatColor.translateAlternateColorCodes('&', input);
		return input;
	}
	
	
	public static String toSaveableFormat(String input) {
		if (input == null) {
			return null;
		}
		
		input = UnicodeSymbols.symbolsToPlaceholders(input);
		input = input.replace("§", "&");
		return input;
	}
	
}
