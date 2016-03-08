package com.gmail.filoghost.holographicdisplays.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.util.Utils;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */
public class PluginHologramManager {

	private static List<PluginHologram> pluginHolograms = Utils.newList();
	
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
		return new ArrayList<PluginHologram>(pluginHolograms);
	}
	
	public static Set<Hologram> getHolograms(Plugin plugin) {
		Set<Hologram> ownedHolograms = Utils.newSet();
		
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
	
	public static void onChunkUnload(Chunk chunk) {
		 // Hide the holograms in that chunk.
		for (PluginHologram hologram : pluginHolograms) {
			if (hologram.isInChunk(chunk)) {
				hologram.despawnEntities();
			}
		}
	}
	
	public static void clearAll() {
		List<PluginHologram> oldHolograms = new ArrayList<PluginHologram>(pluginHolograms);
		pluginHolograms.clear();
		
		for (PluginHologram hologram : oldHolograms) {
			hologram.delete();
		}
	}
}
