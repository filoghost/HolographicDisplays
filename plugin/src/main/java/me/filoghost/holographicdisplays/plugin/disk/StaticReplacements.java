/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.disk;

import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigType;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.logging.ErrorCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class StaticReplacements {

    private static final List<StaticReplacement> replacements = new ArrayList<>();

    public static void load(FileConfig config, ErrorCollector errorCollector) {
        replacements.clear();

        ConfigSection replacementsSection = config.getConfigSection("placeholders");
        if (replacementsSection == null) {
            return;
        }

        for (Entry<ConfigPath, String> entry : replacementsSection.toMap(ConfigType.STRING).entrySet()) {
            String target = entry.getKey().asRawKey();
            String replacement = Colors.addColors(entry.getValue());

            if (target.length() == 0) {
                errorCollector.add("error in \"" + config.getSourceFile() + "\":"
                        + " placeholder cannot be empty (skipped)");
                continue;
            }

            if (target.length() > 100) {
                errorCollector.add("error in \"" + config.getSourceFile() + "\":"
                        + " placeholder cannot be longer than 100 character (" + target + ")");
                continue;
            }

            replacements.add(new StaticReplacement(target, replacement));
        }
    }

    public static String searchAndReplace(String text) {
        for (StaticReplacement replacement : replacements) {
            text = text.replace(replacement.getTarget(), replacement.getReplacement());
        }
        return text;
    }


    private static class StaticReplacement {

        private final String target;
        private final String replacement;

        StaticReplacement(String target, String replacement) {
            this.target = target;
            this.replacement = replacement;
        }

        public String getTarget() {
            return target;
        }

        public String getReplacement() {
            return replacement;
        }

    }

}
