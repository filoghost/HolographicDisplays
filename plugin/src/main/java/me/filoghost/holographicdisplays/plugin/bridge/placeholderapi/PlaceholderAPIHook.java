/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
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

    public static boolean containsPlaceholders(String text) {
        return PlaceholderAPI.containsPlaceholders(text);
    }
    
    public static String replacePlaceholders(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static boolean isEnabled() {
        return enabled;
    }

}
