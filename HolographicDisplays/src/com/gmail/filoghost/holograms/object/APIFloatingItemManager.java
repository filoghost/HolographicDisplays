package com.gmail.filoghost.holograms.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holograms.api.FloatingItem;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */

public class APIFloatingItemManager {

	private static Map<Plugin, List<CraftFloatingItem>> apiFloatingItems = new HashMap<Plugin, List<CraftFloatingItem>>();
	
	public static void onChunkLoad(Chunk chunk) {		
		for (List<CraftFloatingItem> pluginFloatingItemList : apiFloatingItems.values()) {
			for (CraftFloatingItem floatingItem : pluginFloatingItemList) {
				if (floatingItem.isInChunk(chunk)) {
					floatingItem.forceUpdate();
				}
			}
		}
	}
	
	public static void onChunkUnload(Chunk chunk) {		
		for (List<CraftFloatingItem> pluginFloatingItemList : apiFloatingItems.values()) {
			for (CraftFloatingItem floatingItem : pluginFloatingItemList) {
				if (floatingItem.isInChunk(chunk)) {
					floatingItem.hide();
				}
			}
		}
	}
	
	public static void addFloatingItem(Plugin plugin, CraftFloatingItem floatingItem) {
		List<CraftFloatingItem> pluginFloatingItemList = apiFloatingItems.get(plugin);
		if (pluginFloatingItemList == null) {
			pluginFloatingItemList = new ArrayList<CraftFloatingItem>();
			apiFloatingItems.put(plugin, pluginFloatingItemList);
		}
		pluginFloatingItemList.add(floatingItem);
	}
	
	public static void remove(FloatingItem floatingItem) {
		for (List<CraftFloatingItem> pluginFloatingItemList : apiFloatingItems.values()) {
			pluginFloatingItemList.remove(floatingItem);
		}
	}
	
	public static FloatingItem[] getFloatingItems(Plugin plugin) {
		List<CraftFloatingItem> pluginFloatingItemList = apiFloatingItems.get(plugin);
		if (pluginFloatingItemList == null) {
			return new FloatingItem[0];
		} else {
			return pluginFloatingItemList.toArray(new FloatingItem[pluginFloatingItemList.size()]);
			// It's a copy of the original list. Floating items should be removed with delete()
		}
	}
}
