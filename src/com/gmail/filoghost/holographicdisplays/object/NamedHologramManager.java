package com.gmail.filoghost.holographicdisplays.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;

import com.gmail.filoghost.holographicdisplays.util.Utils;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */
public class NamedHologramManager {

	private static List<NamedHologram> pluginHolograms = Utils.newList();
	
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
		return new ArrayList<NamedHologram>(pluginHolograms);
	}
	
	public static NamedHologram getHologram(String name) {
		for (NamedHologram hologram : pluginHolograms) {
			if (hologram.getName().equals(name)) {
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
		List<NamedHologram> oldHolograms = new ArrayList<NamedHologram>(pluginHolograms);
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
