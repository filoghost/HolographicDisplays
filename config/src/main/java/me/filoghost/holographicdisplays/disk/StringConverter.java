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
package me.filoghost.holographicdisplays.disk;

import me.filoghost.holographicdisplays.util.Utils;
import org.bukkit.ChatColor;

public class StringConverter {

	private static final boolean HEX_CHAT_COLORS_ENABLED = Utils.classExists("org.bukkit.entity.Strider");
	private static final int HEX_COLOR_LENGTH = 6;

	public static String toReadableFormat(String input) {
		if (input == null) {
			return null;
		}

		input = UnicodeSymbols.placeholdersToSymbols(input);
		input = addColors(input);
		return input;
	}

	public static String addColors(String input) {
		if (!input.contains("&")) {
			return input;
		}

		StringBuilder output = new StringBuilder(input.length());

		for (int i = 0; i < input.length(); i++) {
			char current = input.charAt(i);

			if (current == '&' && i + 1 < input.length()) {
				char next = input.charAt(i + 1);

				if (next == '#' && HEX_CHAT_COLORS_ENABLED && isValidHexColor(input, i + 2)) {
					output.append(ChatColor.COLOR_CHAR);
					output.append('x');

					for (int j = 0; j < HEX_COLOR_LENGTH; j++) {
						output.append(ChatColor.COLOR_CHAR);
						output.append(Character.toLowerCase(input.charAt(i + 2 + j)));
					}

					i += 1 + HEX_COLOR_LENGTH; // Skip '#' and hex string, which are already converted and added

				} else if (isColorCode(next)) {
					output.append(ChatColor.COLOR_CHAR);
					output.append(Character.toLowerCase(next));

					i++; // Skip next character, which is already added

				} else {
					output.append(current);
				}
			} else {
				output.append(current);
			}
		}

		return output.toString();
	}

	private static boolean isValidHexColor(String input, int startIndex) {
		if (input.length() - startIndex < HEX_COLOR_LENGTH) {
			return false;
		}

		for (int i = 0; i < HEX_COLOR_LENGTH; i++) {
			if (!isHexCode(input.charAt(startIndex + i))) {
				return false;
			}
		}

		return true;
	}

	private static boolean isHexCode(char c) {
		return "0123456789AaBbCcDdEeFf".indexOf(c) > -1;
	}

	private static boolean isColorCode(char c) {
		return "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(c) > -1;
	}
	
}
