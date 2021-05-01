/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api;

import com.gmail.filoghost.holograms.api.internal.NewAPIAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public class HolographicDisplaysAPI {
    
    @Deprecated
    public static Hologram createHologram(Plugin plugin, Location source, String... lines) {
        return NewAPIAdapter.createHologram(plugin, source, lines);
    }

    @Deprecated
    public static FloatingItem createFloatingItem(Plugin plugin, Location source, ItemStack itemstack) {
        return NewAPIAdapter.createFloatingItem(plugin, source, itemstack);
    }

    @Deprecated
    public static Hologram createIndividualHologram(Plugin plugin, Location source, Player whoCanSee, String... lines) {
        return NewAPIAdapter.createIndividualHologram(plugin, source, whoCanSee, lines);
    }
    
    @Deprecated
    public static Hologram createIndividualHologram(Plugin plugin, Location source, List<Player> whoCanSee, String... lines) {
        return NewAPIAdapter.createIndividualHologram(plugin, source, whoCanSee, lines);
    }
    
    @Deprecated
    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, Player whoCanSee, ItemStack itemstack) {
        return NewAPIAdapter.createIndividualFloatingItem(plugin, source, whoCanSee, itemstack);
    }
    
    @Deprecated
    public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, List<Player> whoCanSee, ItemStack itemstack) {
        return NewAPIAdapter.createIndividualFloatingItem(plugin, source, whoCanSee, itemstack);
    }
    
    @Deprecated
    public static Hologram[] getHolograms(Plugin plugin) {
        return NewAPIAdapter.getHolograms(plugin);
    }
    
    @Deprecated
    public static FloatingItem[] getFloatingItems(Plugin plugin) {
        return NewAPIAdapter.getFloatingItems(plugin);
    }
    
    @Deprecated
    public static boolean isHologramEntity(Entity bukkitEntity) {
        return NewAPIAdapter.isHologramEntity(bukkitEntity);
    }

}
