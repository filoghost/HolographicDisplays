package com.gmail.filoghost.holographicdisplays.util;

public class Validator {

	public static void notNull(Object o, String name) {
		if (o == null) {
			throw new NullPointerException(name + " cannot be null");
		}
	}
	
	public static void isTrue(boolean statement, String message) {
		if (!statement) {
			throw new IllegalArgumentException(message);
		}
	}
	
}
