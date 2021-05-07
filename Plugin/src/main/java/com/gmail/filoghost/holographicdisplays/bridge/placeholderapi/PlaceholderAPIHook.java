package com.gmail.filoghost.holographicdisplays.bridge.placeholderapi;

import org.bukkit.entity.Player;

public interface PlaceholderAPIHook {

    boolean containsPlaceholders(String text);

    String replacePlaceholders(Player player, String text);

}
