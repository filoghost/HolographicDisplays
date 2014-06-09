package com.gmail.filoghost.holograms.utils;

import org.bukkit.ChatColor;

import com.gmail.filoghost.holograms.exception.InvalidCharactersException;
import com.gmail.filoghost.holograms.placeholders.StaticPlaceholders;

public class StringUtils {
	
	public static String toReadableFormat(String input) {
		if (input == null || input.length() == 0) return input;

		input = StaticPlaceholders.placeholdersToSymbols(input);
		input = ChatColor.translateAlternateColorCodes('&', input);
		return input;
	}
	
	
	public static String toSaveableFormat(String input) {
		if (input == null || input.length() == 0) return input;
		
		input = StaticPlaceholders.symbolsToPlaceholders(input);
		input = input.replace("§", "&");
		return input;
	}
	
	public static <T> String join(T[] objects, String separator, int start, int end) {
		return org.apache.commons.lang.StringUtils.join(objects, separator, start, end);
	}
	
	
	private static final char[] VALID_HOLOGRAM_NAME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_".toCharArray();
	public static String validateName(String name) throws InvalidCharactersException {
		for (char c : name.toCharArray()) {
			if (!isValidNameChar(c)) {
				throw new InvalidCharactersException(Character.toString(c));
			}
		}
		return name;
	}
	
	private static boolean isValidNameChar(char c) {
		for (char validChar : VALID_HOLOGRAM_NAME_CHARS) {
			if (c == validChar) {
				return true;
			}
		}
		return false;
	}
	
}
