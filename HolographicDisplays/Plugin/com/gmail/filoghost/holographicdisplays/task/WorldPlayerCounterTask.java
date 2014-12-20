package com.gmail.filoghost.holographicdisplays.task;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.util.Utils;

public class WorldPlayerCounterTask implements Runnable {

	private static Map<String, Integer> worlds = Utils.newMap();
	
	@Override
	public void run() {
		worlds.clear();
		
		for (World world : Bukkit.getWorlds()) {
			worlds.put(world.getName(), world.getPlayers().size());
		}
	}
	
	public static String getCount(String world) {
		Integer count = worlds.get(world);
		return count != null ? count.toString() : "[World \"" + world + "\" not found]";
	}
}
