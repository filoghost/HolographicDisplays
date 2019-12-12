package com.gmail.filoghost.holographicdisplays.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook {
    private static boolean enabled;

    public PlaceholderAPIHook() {
        enabled = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static String translate(Player player, String str) {
        if (player == null || !enabled) return str;
        return PlaceholderAPI.setPlaceholders(player, str);
    }
}
