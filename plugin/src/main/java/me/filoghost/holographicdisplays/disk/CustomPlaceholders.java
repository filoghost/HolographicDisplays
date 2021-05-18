/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigType;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.logging.ErrorCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class CustomPlaceholders {

    private static final List<StaticPlaceholder> placeholders = new ArrayList<>();

    public static void load(FileConfig config, ErrorCollector errorCollector) {
        placeholders.clear();

        ConfigSection placeholdersSection = config.getConfigSection("placeholders");
        if (placeholdersSection == null) {
            return;
        }

        for (Entry<ConfigPath, String> entry : placeholdersSection.toMap(ConfigType.STRING).entrySet()) {
            String placeholder = entry.getKey().asRawKey();
            String replacement = Colors.addColors(entry.getValue());

            if (placeholder.length() == 0) {
                errorCollector.add("error in \"" + config.getSourceFile() + "\": placeholder cannot be empty (skipped)");
                continue;
            }

            if (placeholder.length() > 100) {
                errorCollector.add("error in \"" + config.getSourceFile() + "\":" 
                        + " placeholder cannot be longer than 100 character (" + placeholder + ")");
                continue;
            }

            placeholders.add(new StaticPlaceholder(placeholder, replacement));
        }
    }

    public static String replaceStaticPlaceholders(String text) {
        for (StaticPlaceholder staticPlaceholder : placeholders) {
            text = text.replace(staticPlaceholder.getIdentifier(), staticPlaceholder.getReplacement());
        }
        return text;
    }
    

    private static class StaticPlaceholder {
    
        private final String identifier;
        private final String replacement;
    
        StaticPlaceholder(String identifier, String replacement) {
            this.identifier = identifier;
            this.replacement = replacement;
        }
    
        public String getIdentifier() {
            return identifier;
        }
    
        public String getReplacement() {
            return replacement;
        }
    
    }

}
