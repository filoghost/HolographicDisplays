/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */
public class PluginHologramManager {

    private static List<PluginHologram> pluginHolograms = new ArrayList<>();
    
    public static void addHologram(PluginHologram hologram) {
        pluginHolograms.add(hologram);
    }
    
    public static void removeHologram(PluginHologram hologram) {
        pluginHolograms.remove(hologram);
        if (!hologram.isDeleted()) {
            hologram.delete();
        }
    }
    
    public static List<PluginHologram> getHolograms() {
        return new ArrayList<>(pluginHolograms);
    }
    
    public static Set<Hologram> getHolograms(Plugin plugin) {
        Set<Hologram> ownedHolograms = new HashSet<>();
        
        for (PluginHologram hologram : pluginHolograms) {
            if (hologram.getOwner().equals(plugin)) {
                ownedHolograms.add(hologram);
            }
        }
        
        return Collections.unmodifiableSet(ownedHolograms);
    }

    public static void onChunkLoad(Chunk chunk) {
         // Load the holograms in that chunk.
        for (PluginHologram hologram : pluginHolograms) {
            if (hologram.isInChunk(chunk)) {
                hologram.spawnEntities();
            }
        }
    }

}
