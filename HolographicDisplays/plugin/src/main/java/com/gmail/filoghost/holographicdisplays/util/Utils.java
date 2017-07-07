package com.gmail.filoghost.holographicdisplays.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;

public class Utils extends Object {

	/**
	 * Converts a generic array to an array of Strings using the method toString().
	 * @param array the array to convert
	 * @return the new generated array of Strings
	 */
	public static String[] arrayToStrings(Object... array) {
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i] != null ? array[i].toString() : null;
		}
		
		return result;
	}
	
	
	/**
	 * Convenience method to add colors to a string.
	 * @param text the text to colorize
	 * @return the colorized text, or null if text was null
	 */
	public static String addColors(String text) {
		if (text == null) {
			return null;
		}
		
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	
	public static boolean containsIgnoreCase(String toCheck, String content) {
		return toCheck.toLowerCase().contains(content.toLowerCase());
	}
	
	
	public static <T, V> Map<T, V> newMap() {
		return new HashMap<T, V>();
	}
	
	public static <T> List<T> newList() {
		return new ArrayList<T>();
	}
	
	
	public static <T> Set<T> newSet() {
		return new HashSet<T>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] listToArray(List<T> list) {
		return (T[]) list.toArray();
	}
	
	public static int floor(double num) {
		int floor = (int) num;
		return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
	}
	
	
	public static double square(double num) {
		return num * num;
	}
	
	
	public static String join(String[] elements, String separator, int startIndex, int endIndex) {
		Validator.isTrue(startIndex >= 0 && startIndex < elements.length, "startIndex out of bounds");
		Validator.isTrue(endIndex >= 0 && endIndex <= elements.length, "endIndex out of bounds");
		Validator.isTrue(startIndex <= endIndex, "startIndex lower than endIndex");
		
		StringBuilder result = new StringBuilder();
		
		while (startIndex < endIndex) {
			if (result.length() != 0) {
				result.append(separator);
			}
			
			if (elements[startIndex] != null) {
				result.append(elements[startIndex]);
			}
			startIndex++;
		}
		
		return result.toString();
	}

	public static String join(String[] elements, String separator) {
		return join(elements, separator, 0, elements.length);
	}

	public static String join(List<String> elements, String separator, int startIndex, int size) {
		return join(elements.toArray(new String[elements.size()]), separator, startIndex, size);
	}
	
	public static String join(List<String> elements, String separator) {
		return join(elements, separator, 0, elements.size());
	}
	
	public static String sanitize(String s) {
		return s != null ? s : "null";
	}
	
	public static boolean isThereNonNull(Object... objects) {
		if (objects == null) {
			return false;
		}
		
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				return true;
			}
		}
		
		return false;
	}
}
