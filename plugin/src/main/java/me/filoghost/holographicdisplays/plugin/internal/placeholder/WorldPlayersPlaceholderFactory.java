/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderFactory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class WorldPlayersPlaceholderFactory implements GlobalPlaceholderFactory {

    @Override
    public GlobalPlaceholder getPlaceholder(@Nullable String argument) {
        if (argument == null) {
            return new ImmutablePlaceholder("[No world specified]");
        }

        String[] worldNames = Strings.splitAndTrim(argument, ",");
        return new WorldPlayersPlaceholder(worldNames);
    }


    private static class WorldPlayersPlaceholder implements GlobalPlaceholder {

        private final String[] worldNames;

        WorldPlayersPlaceholder(String[] worldNames) {
            this.worldNames = worldNames;
        }

        @Override
        public int getRefreshIntervalTicks() {
            return 20;
        }

        @Override
        public String getReplacement(@Nullable String argument) {
            int count = 0;

            for (String worldName : worldNames) {
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    return "[World \"" + worldName + "\" not found]";
                }

                for (Player player : world.getPlayers()) {
                    if (!player.hasMetadata("NPC")) {
                        count++;
                    }
                }
            }

            return String.valueOf(count);
        }

    }

}
