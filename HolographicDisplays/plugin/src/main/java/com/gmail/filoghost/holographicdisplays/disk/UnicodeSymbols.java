package com.gmail.filoghost.holographicdisplays.disk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.util.FileUtils;

public class UnicodeSymbols {

	private static Map<String, String> placeholders = new HashMap<String, String>();
	
	public static void load(Plugin plugin) {
		placeholders.clear();
		
		File file = new File(plugin.getDataFolder(), "symbols.yml");
		
		if (!file.exists()) {
			plugin.getDataFolder().mkdirs();
			plugin.saveResource("symbols.yml", true);
		}
		
		List<String> lines;
		try {
			lines = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().warning("I/O error while reading symbols.yml. Was the file in use?");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			plugin.getLogger().warning("Unhandled exception while reading symbols.yml!");
			return;
		}
		
		for (String line : lines) {
			
			// Comment or empty line.
			if (line.length() == 0 || line.startsWith("#"))  {
				continue;
			}
			
			if (!line.contains(":")) {
				plugin.getLogger().warning("Unable to parse a line(" + line + ") from symbols.yml: it must contain ':' to separate the placeholder and the replacement.");
				continue;
			}
				
			int indexOf = line.indexOf(':');
			String placeholder = unquote(line.substring(0, indexOf).trim());
			String replacement = StringEscapeUtils.unescapeJava(unquote(line.substring(indexOf + 1, line.length()).trim()));

			if (placeholder.isEmpty() || replacement.isEmpty()) {
				plugin.getLogger().warning("Unable to parse a line(" + line + ") from symbols.yml: the placeholder and the replacement must have both at least 1 character.");
				continue;
			}
			
			if (placeholder.length() > 30) {
				plugin.getLogger().warning("Unable to parse a line(" + line + ") from symbols.yml: the placeholder cannot be longer than 30 characters.");
				continue;
			}
			
			placeholders.put(placeholder, replacement);
		}
	}
	
	
	protected static String placeholdersToSymbols(String input) {
		for (Entry<String, String> entry : placeholders.entrySet()) {
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}
	
	
	protected static String symbolsToPlaceholders(String input) {
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
