/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.holographicdisplays.common.ConsoleLogger;
import me.filoghost.holographicdisplays.util.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

public class UnicodeSymbols {

    private static Map<String, String> placeholders = new HashMap<>();
    
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
            ConsoleLogger.log(Level.WARNING, "I/O error while reading symbols.yml. Was the file in use?", e);
            return;
        } catch (Exception e) {
            ConsoleLogger.log(Level.WARNING, "Unhandled exception while reading symbols.yml!", e);
            return;
        }
        
        for (String line : lines) {
            
            // Comment or empty line.
            if (line.length() == 0 || line.startsWith("#"))  {
                continue;
            }
            
            if (!line.contains(":")) {
                ConsoleLogger.log(Level.WARNING, "Unable to parse a line(" + line + ") from symbols.yml: it must contain ':' to separate the placeholder and the replacement.");
                continue;
            }
                
            int indexOf = line.indexOf(':');
            String placeholder = unquote(line.substring(0, indexOf).trim());
            String replacement = StringEscapeUtils.unescapeJava(unquote(line.substring(indexOf + 1, line.length()).trim()));

            if (placeholder.isEmpty() || replacement.isEmpty()) {
                ConsoleLogger.log(Level.WARNING, "Unable to parse a line(" + line + ") from symbols.yml: the placeholder and the replacement must have both at least 1 character.");
                continue;
            }
            
            if (placeholder.length() > 30) {
                ConsoleLogger.log(Level.WARNING, "Unable to parse a line(" + line + ") from symbols.yml: the placeholder cannot be longer than 30 characters.");
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
