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
package com.gmail.filoghost.holographicdisplays.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldPlayerCounterTask implements Runnable {

	private static Map<String, Integer> worlds = new HashMap<>();
	
	@Override
	public void run() {
		worlds.clear();
		
		for (World world : Bukkit.getWorlds()) {
			List<Player> players = world.getPlayers();
			int count = 0;
			
			for (Player player : players) {
				if (!player.hasMetadata("NPC")) {
					count++;
				}
			}
			worlds.put(world.getName(), count);
		}
	}
	
	public static String getCount(String[] worldsNames) {
		int total = 0;
		for (String worldName : worldsNames) {
			Integer count = worlds.get(worldName);
			if (count == null) {
				return "[World \"" + worldName + "\" not found]";
			}
			
			total += count;
		}
		
		return String.valueOf(total);
	}
	
	public static String getCount(String worldName) {
		Integer count = worlds.get(worldName);
		return count != null ? count.toString() : "[World \"" + worldName + "\" not found]";
	}
}
