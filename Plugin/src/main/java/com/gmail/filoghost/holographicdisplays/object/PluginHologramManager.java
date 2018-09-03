/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
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
