/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ActiveObjectsRegistry {

    private static final Map<String, List<HologramAdapter>> hologramsByPlugin = new HashMap<>();
    private static final Map<String, List<FloatingItemAdapter>> floatingItemsByPlugin = new HashMap<>();

    static void addHologram(HologramAdapter hologram) {
        hologramsByPlugin.computeIfAbsent(hologram.getOwnerPluginName(), key -> new ArrayList<>()).add(hologram);
    }

    static void removeHologram(HologramAdapter hologram) {
        hologramsByPlugin.get(hologram.getOwnerPluginName()).remove(hologram);
    }

    static List<HologramAdapter> getHolograms(Plugin plugin) {
        return hologramsByPlugin.getOrDefault(plugin.getName(), Collections.emptyList());
    }

    static void addFloatingItem(FloatingItemAdapter floatingItem) {
        floatingItemsByPlugin.computeIfAbsent(floatingItem.getOwnerPluginName(), key -> new ArrayList<>()).add(floatingItem);
    }

    static void removeFloatingItem(FloatingItemAdapter floatingItem) {
        floatingItemsByPlugin.get(floatingItem.getOwnerPluginName()).remove(floatingItem);
    }
    
    static List<FloatingItemAdapter> getFloatingItems(Plugin plugin) {
        return floatingItemsByPlugin.getOrDefault(plugin.getName(), Collections.emptyList());
    }

}
