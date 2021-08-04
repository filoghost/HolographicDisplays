/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.filoghost.fcommons.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook {

    private static boolean enabled;

    public static void setup() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return;
        }

        enabled = true;
    }

    public static boolean containsPlaceholderPattern(String text) {
        if (Strings.isEmpty(text)) {
            return false;
        }

        int firstIndex = text.indexOf('%');
        if (firstIndex < 0) {
            return false;
        }

        int lastIndex = text.lastIndexOf('%');
        return lastIndex - firstIndex >= 2; // At least one character between the two indexes
    }

    public static String replacePlaceholders(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static boolean isEnabled() {
        return enabled;
    }

}
