/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.Hologram;
import me.filoghost.fcommons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("deprecation")
public class NewAPIAdapter {

    private static final Set<String> notifiedPlugins = new HashSet<>();

    public static Hologram createHologram(Plugin plugin, Location source, String... lines) {
        validatePluginAndNotify(plugin);
        validateSource(source);
        validateLines(lines);

        return new HologramAdapter(plugin, source, lines);
    }

    public static FloatingItem createFloatingItem(Plugin plugin, Location source, ItemStack itemstack) {
        validatePluginAndNotify(plugin);
        validateSource(source);
        validateItem(itemstack);

        return new FloatingItemAdapter(plugin, source, itemstack);
    }

    public static Hologram createIndividualHologram(Plugin plugin, Location source, Player whoCanSee, String... lines) {
        return createIndividualHologram(plugin, source, Collections.singletonList(whoCanSee), lines);
    }

    public static Hologram createIndividualHologram(Plugin plugin, Location source, List<Player> whoCanSee, String... lines) {
        validatePluginAndNotify(plugin);
        validateSource(source);
        validateLines(lines);

        return new HologramAdapter(plugin, source, lines, whoCanSee);
    }

    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, Player whoCanSee, ItemStack itemstack) {
        return createIndividualFloatingItem(plugin, source, Collections.singletonList(whoCanSee), itemstack);
    }

    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, List<Player> whoCanSee, ItemStack itemstack) {
        validatePluginAndNotify(plugin);
        validateSource(source);
        validateItem(itemstack);

        return new FloatingItemAdapter(plugin, source, itemstack, whoCanSee);
    }

    public static Hologram[] getHolograms(Plugin plugin) {
        validatePluginAndNotify(plugin);

        List<HologramAdapter> pluginHolograms = ActiveObjectsRegistry.getHolograms(plugin);
        return pluginHolograms.toArray(new HologramAdapter[0]);
    }

    public static FloatingItem[] getFloatingItems(Plugin plugin) {
        validatePluginAndNotify(plugin);

        List<FloatingItemAdapter> pluginFloatingItems = ActiveObjectsRegistry.getFloatingItems(plugin);
        return pluginFloatingItems.toArray(new FloatingItemAdapter[0]);
    }

    public static boolean isHologramEntity(Entity bukkitEntity) {
        return me.filoghost.holographicdisplays.api.HolographicDisplaysAPI.isHologramEntity(bukkitEntity);
    }

    private static void validatePluginAndNotify(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");

        if (notifiedPlugins.add(plugin.getName())) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Holographic Displays] The plugin \""
                    + plugin.getName() + "\" is still using the old v1 API of Holographic Displays."
                    + " Please notify its author, as it's not guaranteed to work in the future.");
        }
    }

    private static void validateSource(Location source) {
        Preconditions.notNull(source, "source");
        Preconditions.notNull(source.getWorld(), "source's world");
    }

    private static void validateLines(String[] lines) {
        Preconditions.notNull(lines, "lines");
    }

    private static void validateItem(ItemStack itemstack) {
        Preconditions.notNull(itemstack, "itemstack");
        Preconditions.checkArgument(itemstack.getType() != Material.AIR, "itemstack cannot be AIR");
    }

}
