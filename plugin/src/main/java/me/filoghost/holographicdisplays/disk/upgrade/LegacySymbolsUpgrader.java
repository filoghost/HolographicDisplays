/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk.upgrade;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LegacySymbolsUpgrader {
    
    public static void run(ConfigManager configManager) throws ConfigLoadException, ConfigSaveException {
        Path oldFile = configManager.getRootDataFolder().resolve("symbols.yml");
        ConfigLoader newConfigLoader = configManager.getConfigLoader("custom-placeholders.yml");
        Path newFile = newConfigLoader.getFile();

        if (!Files.isRegularFile(oldFile)) {
            return; // Old file doesn't exist, ignore upgrade
        }
        
        if (Files.isRegularFile(newFile)) {
            return; // Already created, do not override
        }

        Config newConfig = new Config();
        List<String> lines;
        try {
            lines = Files.readAllLines(oldFile);
        } catch (IOException e) {
            throw new ConfigLoadException(ConfigErrors.readIOException, e);
        }

        for (String line : lines) {
            // Comment or empty line
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // Ignore bad line
            if (!line.contains(":")) {
                Log.warning("Couldn't convert invalid line in " + oldFile.getFileName() + ": " + line);
                continue;
            }

            String[] parts = Strings.splitAndTrim(line, ":", 2);
            String placeholder = unquote(parts[0]);
            String replacement = StringEscapeUtils.unescapeJava(unquote(parts[1]));

            newConfig.setString("placeholders." + placeholder, replacement);
        }

        try {
            Files.move(oldFile, oldFile.resolveSibling("symbols.yml.backup"));
        } catch (IOException e) {
            Log.warning("Couldn't rename " + oldFile.getFileName(), e);
        }
        newConfigLoader.save(newConfig);
    }

    private static String unquote(String input) {
        if (input.length() < 2) {
            // Too short, cannot be a quoted string
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
