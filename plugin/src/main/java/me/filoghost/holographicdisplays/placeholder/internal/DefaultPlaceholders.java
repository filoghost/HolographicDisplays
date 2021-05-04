/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.internal;

import me.filoghost.fcommons.collection.CollectionUtils;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.bridge.bungeecord.ServerInfo;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class DefaultPlaceholders {

    private static final String PINGER_NOT_ENABLED_ERROR = "[Please enable pinger]";
    public static final String NO_SERVER_SPECIFIED_ERROR = "[No server specified]";

    public static void resetAndRegister(PlaceholderRegistry placeholderRegistry, AnimationRegistry animationRegistry, BungeeServerTracker bungeeServerTracker) {
        HolographicDisplays plugin = HolographicDisplays.getInstance();
        placeholderRegistry.unregisterAll(plugin);

        // TODO restore "&u"
        placeholderRegistry.register(plugin, "rainbow", new AnimationPlaceholder(4, toStringList(
                ChatColor.RED,
                ChatColor.GOLD,
                ChatColor.YELLOW,
                ChatColor.GREEN,
                ChatColor.AQUA,
                ChatColor.LIGHT_PURPLE
        )));

        placeholderRegistry.registerReplacer(plugin, "time", 10, (argument) -> {
            return Configuration.timeFormat.format(Instant.now());
        });

        placeholderRegistry.registerFactory(plugin, "animation", animationRegistry);
        
        placeholderRegistry.registerFactory(plugin, "world", new WorldPlayersPlaceholderFactory());
        
        placeholderRegistry.registerFactory(plugin, "online", new OnlinePlayersPlaceholderFactory(bungeeServerTracker));
        
        placeholderRegistry.registerReplacer(plugin, "max_players", 20, (serverName) -> {
            if (serverName == null) {
                // No argument specified, return max players of this server
                return String.valueOf(Bukkit.getMaxPlayers());
            }
            
            if (!Configuration.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            return String.valueOf(bungeeServerTracker.getCurrentServerInfo(serverName).getMaxPlayers());
        });

        placeholderRegistry.registerReplacer(plugin, "status", 20, (serverName) -> {
            if (serverName == null) {
                return NO_SERVER_SPECIFIED_ERROR;
            }
            
            if (!Configuration.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            ServerInfo serverInfo = bungeeServerTracker.getCurrentServerInfo(serverName);
            if (serverInfo.isOnline()) {
                return Configuration.pingerStatusOnline;
            } else {
                return Configuration.pingerStatusOffline;
            }
        });

        placeholderRegistry.registerReplacer(plugin, "motd", 20, (serverName) -> {
            if (serverName == null) {
                return NO_SERVER_SPECIFIED_ERROR;
            }
            
            if (!Configuration.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            return bungeeServerTracker.getCurrentServerInfo(serverName).getMotdLine1();
        });

        placeholderRegistry.registerReplacer(plugin, "motd2", 20, (serverName) -> {
            if (serverName == null) {
                return NO_SERVER_SPECIFIED_ERROR;
            }
            
            if (!Configuration.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            return bungeeServerTracker.getCurrentServerInfo(serverName).getMotdLine2();
        });
    }

    private static List<String> toStringList(ChatColor... colors) {
        return CollectionUtils.toArrayList(Arrays.asList(colors), ChatColor::toString);
    }

}
