package com.gmail.filoghost.holograms.utils;

public class Validator {

	public static void notNull(Object o, String message) {
		if (o == null) {
			throw new NullPointerException(message);
		}
	}
	
	public static void checkArgument(boolean b, String message) {
		if (!b) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void checkState(boolean b, String message) {
		if (!b) {
			throw new IllegalStateException(message);
		}
	}
	
}
