package com.gmail.filoghost.holograms.utils;

public class GenericUtils {
	
	public static <T> String[] arrayToStrings(T... array) {
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i] != null ? array[i].toString() : null;
		}
		
		return result;
	}
	
	public static double square(double d) {
		return d * d;
	}
}
