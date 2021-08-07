/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.plugin.config.ConfigManager;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SymbolsLegacyUpgrade extends LegacyUpgrade {

    private final Path oldFile;

    public SymbolsLegacyUpgrade(ConfigManager configManager, ErrorCollector errorCollector) {
        super(configManager, errorCollector);
        this.oldFile = configManager.getRootDataFolder().resolve("symbols.yml");
    }

    @Override
    protected Path getFile() {
        return oldFile;
    }

    @Override
    public void run() throws IOException, ConfigException {
        ConfigLoader newConfigLoader = configManager.getConfigLoader("custom-placeholders.yml");
        Path newFile = newConfigLoader.getFile();

        if (!Files.isRegularFile(oldFile)) {
            return; // Old file doesn't exist, nothing to upgrade
        }

        if (Files.isRegularFile(newFile)) {
            return; // Already existing, do not override
        }

        Config newConfig = new Config();
        ConfigSection placeholdersSection = newConfig.getOrCreateSection("placeholders");

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
                continue;
            }

            String[] placeholderAndReplacement = Strings.splitAndTrim(line, ":", 2);
            String placeholder = unquote(placeholderAndReplacement[0]);
            String replacement = StringEscapeUtils.unescapeJava(unquote(placeholderAndReplacement[1]));

            placeholdersSection.setString(ConfigPath.literal(placeholder), replacement);
        }

        createBackupFile(oldFile);
        newConfigLoader.save(newConfig);
        Files.delete(oldFile);
    }

    private static String unquote(String input) {
        if (input.length() < 2) {
            // Too short to be a quoted string
            return input;
        }

        if ((input.startsWith("'") && input.endsWith("'")) || (input.startsWith("\"") && input.endsWith("\""))) {
            return input.substring(1, input.length() - 1);
        } else {
            return input;
        }
    }

}
