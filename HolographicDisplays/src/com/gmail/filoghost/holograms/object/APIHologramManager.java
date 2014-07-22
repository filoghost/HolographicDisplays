package com.gmail.filoghost.holograms.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holograms.api.Hologram;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */

public class APIHologramManager {

	private static Map<Plugin, List<CraftHologram>> apiHolograms = new HashMap<Plugin, List<CraftHologram>>();
	
	public static void onChunkLoad(Chunk chunk) {		
		for (List<CraftHologram> pluginHologramList : apiHolograms.values()) {
			for (CraftHologram hologram : pluginHologramList) {
				if (hologram.isInChunk(chunk)) {
					hologram.forceUpdate();
				}
			}
		}
	}
	
	public static void onChunkUnload(Chunk chunk) {		
		for (List<CraftHologram> pluginHologramList : apiHolograms.values()) {
			for (CraftHologram hologram : pluginHologramList) {
				if (hologram.isInChunk(chunk)) {
					hologram.hide();
				}
			}
		}
	}
	
	public static void addHologram(Plugin plugin, CraftHologram hologram) {
		List<CraftHologram> pluginHologramList = apiHolograms.get(plugin);
		if (pluginHologramList == null) {
			pluginHologramList = new ArrayList<CraftHologram>();
			apiHolograms.put(plugin, pluginHologramList);
		}
		pluginHologramList.add(hologram);
	}
	
	public static void remove(Hologram hologram) {
		for (List<CraftHologram> pluginHologramList : apiHolograms.values()) {
			pluginHologramList.remove(hologram);
		}
	}
	
	public static Hologram[] getHolograms(Plugin plugin) {
		List<CraftHologram> pluginHologramList = apiHolograms.get(plugin);
		if (pluginHologramList == null) {
			return new Hologram[0];
		} else {
			return pluginHologramList.toArray(new Hologram[pluginHologramList.size()]);
			// It's a copy of the original list. Holograms should be removed with delete()
		}
	}
}
