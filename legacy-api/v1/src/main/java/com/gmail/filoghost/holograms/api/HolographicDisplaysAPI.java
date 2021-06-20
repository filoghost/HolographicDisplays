/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public class HolographicDisplaysAPI {

    private static final Set<String> notifiedPlugins = new HashSet<>();

    @Deprecated
    public static Hologram createHologram(Plugin plugin, Location source, String... lines) {
        throw removedAPIException(plugin);
    }

    @Deprecated
    public static FloatingItem createFloatingItem(Plugin plugin, Location source, ItemStack itemstack) {
        throw removedAPIException(plugin);
    }

    @Deprecated
    public static Hologram createIndividualHologram(Plugin plugin, Location source, Player whoCanSee, String... lines) {
        throw removedAPIException(plugin);
    }

    @Deprecated
    public static Hologram createIndividualHologram(Plugin plugin, Location source, List<Player> whoCanSee, String... lines) {
        throw removedAPIException(plugin);
    }

    @Deprecated
    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, Player whoCanSee, ItemStack itemstack) {
        throw removedAPIException(plugin);
    }

    @Deprecated
    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, List<Player> whoCanSee, ItemStack itemstack) {
        throw removedAPIException(plugin);
    }

    @Deprecated
    public static Hologram[] getHolograms(Plugin plugin) {
        throw removedAPIException(plugin);
    }

    @Deprecated
    public static FloatingItem[] getFloatingItems(Plugin plugin) {
        throw removedAPIException(plugin);
    }

    private static RuntimeException removedAPIException(Plugin plugin) {
        if (plugin != null && notifiedPlugins.add(plugin.getName())) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Holographic Displays] The plugin \""
                    + plugin.getName() + "\" is still using the API v1 of Holographic Displays,"
                    + "which has been removed. Please notify its author.");
        }

        return new IllegalStateException("The legacy Holographic Displays API v1 has been removed");
    }

    @Deprecated
    public static boolean isHologramEntity(Entity bukkitEntity) {
        return me.filoghost.holographicdisplays.api.HolographicDisplaysAPI.isHologramEntity(bukkitEntity);
    }

}
