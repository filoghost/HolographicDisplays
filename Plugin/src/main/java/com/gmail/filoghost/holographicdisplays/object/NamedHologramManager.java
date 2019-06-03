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
import java.util.List;

import org.bukkit.Chunk;

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
