/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */
public class NamedHologramManager {

    private static List<NamedHologram> pluginHolograms = new ArrayList<>();
    
    public static void addHologram(NamedHologram hologram) {
        pluginHolograms.add(hologram);
    }
    
    public static void removeHologram(NamedHologram hologram) {
        pluginHolograms.remove(hologram);
        if (!hologram.isDeleted()) {
            hologram.delete();
        }
    }
    
    public static List<NamedHologram> getHolograms() {
        return new ArrayList<>(pluginHolograms);
    }
    
    public static NamedHologram getHologram(String name) {
        for (NamedHologram hologram : pluginHolograms) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                return hologram;
            }
        }
        return null;
    }
    
    public static boolean isExistingHologram(String name) {
        return (getHologram(name) != null);
    }

    public static void onChunkLoad(Chunk chunk) {
         // Load the holograms in that chunk.
        for (NamedHologram hologram : pluginHolograms) {
            if (hologram.isInChunk(chunk)) {
                hologram.spawnEntities();
            }
        }
    }
    
    public static void onChunkUnload(Chunk chunk) {
         // Hide the holograms in that chunk.
        for (NamedHologram hologram : pluginHolograms) {
            if (hologram.isInChunk(chunk)) {
                hologram.despawnEntities();
            }
        }
    }
    
    public static void clearAll() {
        List<NamedHologram> oldHolograms = new ArrayList<>(pluginHolograms);
        pluginHolograms.clear();
        
        for (NamedHologram hologram : oldHolograms) {
            hologram.delete();
        }
    }

    public static int size() {
        return pluginHolograms.size();
    }

    public static NamedHologram get(int i) {
        return pluginHolograms.get(i);
    }
}
