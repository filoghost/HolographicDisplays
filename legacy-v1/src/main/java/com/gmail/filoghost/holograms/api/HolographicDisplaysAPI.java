/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api;

import com.gmail.filoghost.holograms.api.adapter.FloatingItemAdapter;
import com.gmail.filoghost.holograms.api.adapter.HologramAdapter;
import me.filoghost.holographicdisplays.api.HologramsAPI;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.common.Validator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public class HolographicDisplaysAPI {
    
    private static Set<String> notifiedPlugins = new HashSet<>();
    
    private static void notifyOldAPI(Plugin plugin) {
        Validator.notNull(plugin, "plugin");
        
        if (notifiedPlugins.add(plugin.getName())) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Holographic Displays] The plugin \"" + plugin.getName() + "\" is still using the old API of Holographic Displays. "
                + "Please notify the author and ask them to update it, the old API will be removed soon.");
        }
    }
    
    @Deprecated
    public static Hologram createHologram(Plugin plugin, Location source, String... lines) {
        notifyOldAPI(plugin);
        
        validateLocation(source);
        
        me.filoghost.holographicdisplays.api.Hologram hologram = HologramsAPI.createHologram(plugin, source);
        for (String line : lines) {
            hologram.appendTextLine(line);
        }
        return new HologramAdapter(plugin, hologram);
    }

    @Deprecated
    public static FloatingItem createFloatingItem(Plugin plugin, Location source, ItemStack itemstack) {
        notifyOldAPI(plugin);
        
        validateLocation(source);
        validateItem(itemstack);
        
        me.filoghost.holographicdisplays.api.Hologram hologram = HologramsAPI.createHologram(plugin, source);
        ItemLine itemLine = hologram.appendItemLine(itemstack);
        return new FloatingItemAdapter(plugin, hologram, itemLine);
    }

    @Deprecated
    public static Hologram createIndividualHologram(Plugin plugin, Location source, Player whoCanSee, String... lines) {
        notifyOldAPI(plugin);
        
        return createIndividualHologram(plugin, source, Arrays.asList(whoCanSee), lines);
    }
    
    @Deprecated
    public static Hologram createIndividualHologram(Plugin plugin, Location source, List<Player> whoCanSee, String... lines) {
        notifyOldAPI(plugin);
        
        validateLocation(source);
        
        me.filoghost.holographicdisplays.api.Hologram hologram = HologramsAPI.createHologram(plugin, source);
        
        hologram.getVisibilityManager().setVisibleByDefault(false);
        if (whoCanSee != null) {
            for (Player player : whoCanSee) {
                hologram.getVisibilityManager().showTo(player);
            }
        }
        
        for (String line : lines) {
            hologram.appendTextLine(line);
        }
        
        return new HologramAdapter(plugin, hologram);
    }
    
    @Deprecated
    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, Player whoCanSee, ItemStack itemstack) {
        notifyOldAPI(plugin);
        
        return createIndividualFloatingItem(plugin, source, Arrays.asList(whoCanSee), itemstack);
    }
    
    @Deprecated
    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, List<Player> whoCanSee, ItemStack itemstack) {
        notifyOldAPI(plugin);
        
        validateLocation(source);
        validateItem(itemstack);
        
        me.filoghost.holographicdisplays.api.Hologram hologram = HologramsAPI.createHologram(plugin, source);
        ItemLine itemLine = hologram.appendItemLine(itemstack);
        
        hologram.getVisibilityManager().setVisibleByDefault(false);
        if (whoCanSee != null) {
            for (Player player : whoCanSee) {
                hologram.getVisibilityManager().showTo(player);
            }
        }
        
        return new FloatingItemAdapter(plugin, hologram, itemLine);
    }
    
    @Deprecated
    public static Hologram[] getHolograms(Plugin plugin) {
        notifyOldAPI(plugin);
        
        Collection<HologramAdapter> pluginHolograms = HologramAdapter.activeHolograms.getOrDefault(plugin, Collections.emptyList());        
        return pluginHolograms.toArray(new HologramAdapter[0]);
    }
    
    @Deprecated
    public static FloatingItem[] getFloatingItems(Plugin plugin) {
        notifyOldAPI(plugin);
        
        Collection<FloatingItemAdapter> pluginFloatingItems = FloatingItemAdapter.activeFloatingItems.getOrDefault(plugin, Collections.emptyList());        
        return pluginFloatingItems.toArray(new FloatingItemAdapter[0]);
    }
    
    @Deprecated
    public static boolean isHologramEntity(Entity bukkitEntity) {
        return HologramsAPI.isHologramEntity(bukkitEntity);
    }
    
    private static void validateLocation(Location loc) {
        Validator.notNull(loc, "location");
        Validator.notNull(loc.getWorld(), "location's world");
    }
    
    private static void validateItem(ItemStack itemstack) {
        Validator.notNull(itemstack, "itemstack");
        Validator.isTrue(itemstack.getType() != Material.AIR, "itemstack cannot be AIR");
    }

}
