/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class CustomPlaceholders {

    private static final List<StaticPlaceholder> placeholders = new ArrayList<>();

    public static void load(FileConfig config) {
        placeholders.clear();

        ConfigSection placeholdersSection = config.getConfigSection("placeholders");
        if (placeholdersSection == null) {
            return;
        }

        for (String placeholder : placeholdersSection.getKeys()) {
            String replacement = Colors.addColors(placeholdersSection.getString(placeholder));
            if (replacement == null) {
                return;
            }

            if (placeholder.length() == 0) {
                Log.warning("Error in \"" + config.getSourceFile() + "\": placeholder cannot be empty (skipped).");
                continue;
            }

            if (placeholder.length() > 100) {
                Log.warning("Error in \"" + config.getSourceFile() + "\": placeholder cannot be longer than 100 character (" + placeholder + ").");
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
    
        public StaticPlaceholder(String identifier, String replacement) {
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
