/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.internal;

import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.BungeeServerTracker;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class OnlinePlayersPlaceholderFactory implements PlaceholderFactory {

    private final BungeeServerTracker bungeeServerTracker;

    public OnlinePlayersPlaceholderFactory(BungeeServerTracker bungeeServerTracker) {
        this.bungeeServerTracker = bungeeServerTracker;
    }

    @Override
    public Placeholder getPlaceholder(@Nullable String argument) {
        if (argument == null) {
            // No argument specified, return online players in this server
            return new LocalOnlinePlayersPlaceholder();
        }
        
        String[] serverNames = Strings.splitAndTrim(argument, ",");
        return new BungeeOnlinePlayersPlaceholder(serverNames, bungeeServerTracker);
    }
    
    
    private static class LocalOnlinePlayersPlaceholder implements Placeholder {

        @Override
        public int getRefreshIntervalTicks() {
            return 20;
        }

        @Override
        public String getReplacement(@Nullable String argument) {
            return String.valueOf(Bukkit.getOnlinePlayers().size());
        }

    }


    private static class BungeeOnlinePlayersPlaceholder implements Placeholder {

        private final String[] serverNames;
        private final BungeeServerTracker bungeeServerTracker;

        BungeeOnlinePlayersPlaceholder(String[] serverNames, BungeeServerTracker bungeeServerTracker) {
            this.serverNames = serverNames;
            this.bungeeServerTracker = bungeeServerTracker;
        }

        @Override
        public int getRefreshIntervalTicks() {
            return 20;
        }

        @Override
        public String getReplacement(@Nullable String argument) {
            int count = 0;
            for (String serverName : serverNames) {
                count += bungeeServerTracker.getCurrentServerInfo(serverName).getOnlinePlayers();
            }

            return String.valueOf(count);
        }

    }

}
