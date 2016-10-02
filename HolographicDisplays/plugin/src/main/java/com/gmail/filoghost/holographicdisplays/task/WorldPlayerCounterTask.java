package com.gmail.filoghost.holographicdisplays.task;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.util.Utils;

public class WorldPlayerCounterTask implements Runnable {

	private static Map<String, Integer> worlds = Utils.newMap();
	
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
	
	public static String getCount(String world) {
		Integer count = worlds.get(world);
		return count != null ? count.toString() : "[World \"" + world + "\" not found]";
	}
}
