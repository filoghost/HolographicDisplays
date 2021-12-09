/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import me.filoghost.fcommons.collection.CollectionUtils;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.ServerInfo;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class DefaultPlaceholders {

    private static final String PINGER_NOT_ENABLED_ERROR = "[Please enable pinger]";
    private static final String NO_SERVER_SPECIFIED_ERROR = "[No server specified]";

    public static void resetAndRegister(
            Plugin plugin,
            PlaceholderRegistry placeholderRegistry,
            AnimationPlaceholderFactory animationPlaceholderFactory,
            BungeeServerTracker bungeeServerTracker) {
        placeholderRegistry.unregisterAll(plugin);

        placeholderRegistry.registerGlobalPlaceholder(plugin, "empty", Integer.MAX_VALUE, (argument) -> {
            return "";
        });

        placeholderRegistry.registerGlobalPlaceholder(plugin, "rainbow", new AnimationPlaceholder(4, toStringList(
                ChatColor.RED,
                ChatColor.GOLD,
                ChatColor.YELLOW,
                ChatColor.GREEN,
                ChatColor.AQUA,
                ChatColor.LIGHT_PURPLE
        )));

        placeholderRegistry.registerGlobalPlaceholder(plugin, "time", 10, (argument) -> {
            return Settings.timeFormat.format(Instant.now());
        });

        placeholderRegistry.registerGlobalPlaceholderFactory(plugin, "animation", animationPlaceholderFactory);

        placeholderRegistry.registerGlobalPlaceholderFactory(plugin, "world", new WorldPlayersPlaceholderFactory());

        placeholderRegistry.registerGlobalPlaceholderFactory(plugin, "online", new OnlinePlayersPlaceholderFactory(bungeeServerTracker));

        placeholderRegistry.registerGlobalPlaceholder(plugin, "max_players", 20, (serverName) -> {
            if (serverName == null) {
                // No argument specified, return max players of this server
                return String.valueOf(Bukkit.getMaxPlayers());
            }

            if (!Settings.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            return String.valueOf(bungeeServerTracker.getCurrentServerInfo(serverName).getMaxPlayers());
        });

        placeholderRegistry.registerGlobalPlaceholder(plugin, "status", 20, (serverName) -> {
            if (serverName == null) {
                return NO_SERVER_SPECIFIED_ERROR;
            }

            if (!Settings.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            ServerInfo serverInfo = bungeeServerTracker.getCurrentServerInfo(serverName);
            if (serverInfo.isOnline()) {
                return Settings.pingerStatusOnline;
            } else {
                return Settings.pingerStatusOffline;
            }
        });

        placeholderRegistry.registerGlobalPlaceholder(plugin, "motd", 20, (serverName) -> {
            if (serverName == null) {
                return NO_SERVER_SPECIFIED_ERROR;
            }

            if (!Settings.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            return bungeeServerTracker.getCurrentServerInfo(serverName).getMotdLine1();
        });

        placeholderRegistry.registerGlobalPlaceholder(plugin, "motd2", 20, (serverName) -> {
            if (serverName == null) {
                return NO_SERVER_SPECIFIED_ERROR;
            }

            if (!Settings.pingerEnabled) {
                return PINGER_NOT_ENABLED_ERROR;
            }

            return bungeeServerTracker.getCurrentServerInfo(serverName).getMotdLine2();
        });

        placeholderRegistry.registerIndividualPlaceholder(plugin, "player", Integer.MAX_VALUE, (player, argument) -> {
            return player.getName();
        });

        placeholderRegistry.registerIndividualPlaceholder(plugin, "displayName", 20, (player, argument) -> {
            return player.getDisplayName();
        });

        placeholderRegistry.registerIndividualPlaceholderFactory(plugin, "papi", new PlaceholderAPIPlaceholderFactory());
    }

    private static List<String> toStringList(ChatColor... colors) {
        return CollectionUtils.toArrayList(Arrays.asList(colors), ChatColor::toString);
    }

}
