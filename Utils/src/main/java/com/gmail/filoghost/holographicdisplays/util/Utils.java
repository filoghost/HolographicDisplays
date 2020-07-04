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
package com.gmail.filoghost.holographicdisplays.util;

import net.md_5.bungee.api.ChatColor;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class Utils {
	/** Matches a single color code (including hex but not including other formatting such as bold). **/
	private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("§([0-9A-F]|X(§[0-9A-F]){6})", Pattern.CASE_INSENSITIVE);

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


	public static String limitLength(String s, int maxLength) {
		if (s != null && s.length() > maxLength) {
			s = s.substring(0, maxLength);
		}
		return s;
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


	public static boolean classExists(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public static String uncapitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		return new StringBuilder(str.length())
				.append(Character.toLowerCase(str.charAt(0)))
				.append(str.substring(1))
				.toString();
	}

	/**
	 * @return whether the string is a single color code (including hex but not including other formatting such as bold)
	 */
	public static boolean isColorCode(String maybeColorCode) {
		return COLOR_CODE_PATTERN.matcher(maybeColorCode).matches();
	}

	/**
	 * @param colorCode see {@link #isColorCode(String)}
	 */
	public static ChatColor toChatColor(String colorCode) {
		final String lowercaseColorCode = colorCode.toLowerCase(Locale.ROOT);
		return Optional.ofNullable(ChatColor.getByChar(lowercaseColorCode.charAt(1)))
				.orElseGet(() -> ChatColor.of("#" + lowercaseColorCode.replace("§", "").replace("x", "")));
	}
}
