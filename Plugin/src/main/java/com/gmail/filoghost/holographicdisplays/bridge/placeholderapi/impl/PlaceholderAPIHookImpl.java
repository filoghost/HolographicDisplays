package com.gmail.filoghost.holographicdisplays.bridge.placeholderapi.impl;

import com.gmail.filoghost.holographicdisplays.bridge.placeholderapi.PlaceholderAPIHook;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIHookImpl implements PlaceholderAPIHook {
    @Override
    public boolean containsPlaceholders(String text) {
        return PlaceholderAPI.containsPlaceholders(text);
    }

    @Override
    public String replacePlaceholders(Player player, String text) {
        if (player == null) {
            return text;
        }
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
