package com.gmail.filoghost.holograms.placeholders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.utils.FileUtils;

public class StaticPlaceholders {

	private static Map<String, String> placeholders = new HashMap<String, String>();
	
	public static void load() throws FileNotFoundException, IOException, Exception {
		placeholders.clear();
		
		File file = new File(HolographicDisplays.getInstance().getDataFolder(), "placeholders.yml");
		
		if (!file.exists()) {
			HolographicDisplays.getInstance().saveResource("placeholders.yml", false);
		}
		
		List<String> lines = FileUtils.readLines("placeholders.yml");
		for (String line : lines) {
			
			// Comment or empty line.
			if (line.length() == 0 || line.startsWith("#"))  {
				continue;
			}
			
			if (!line.contains(":")) {
				HolographicDisplays.getInstance().getLogger().warning("Unable to parse a line(" + line + ") from placeholders.yml: it must contain ':' to separate the placeholder and the replacement.");
				continue;
			}
				
			int indexOf = line.indexOf(':');
			String placeholder = unquote(line.substring(0, indexOf).trim());
			String replacement = StringEscapeUtils.unescapeJava(unquote(line.substring(indexOf + 1, line.length()).trim()));

			if (placeholder.length() == 0 || replacement.length() == 0) {
				HolographicDisplays.getInstance().getLogger().warning("Unable to parse a line(" + line + ") from placeholders.yml: the placeholder and the replacement must have both at least 1 character.");
				continue;
			}
			
			if (placeholder.length() > 20) {
				HolographicDisplays.getInstance().getLogger().warning("Unable to parse a line(" + line + ") from placeholders.yml: the placeholder cannot be longer than 20 characters.");
				continue;
			}
			
			placeholders.put(placeholder, replacement);
		}
	}
	
	public static String placeholdersToSymbols(String input) {
		for (Entry<String, String> entry : placeholders.entrySet()) {
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}
	
	public static String symbolsToPlaceholders(String input) {
		for (Entry<String, String> entry : placeholders.entrySet()) {
			input = input.replace(entry.getValue(), entry.getKey());
		}
		return input;
	}
	
	private static String unquote(String input) {
		if (input.length() < 2) {
			// Cannot be quoted.
			return input;
		}
		if (input.startsWith("'") && input.endsWith("'")) {
			return input.substring(1, input.length() - 1);
		} else if (input.startsWith("\"") && input.endsWith("\"")) {
			return input.substring(1, input.length() - 1);
		}
		
		return input;
	}
}
