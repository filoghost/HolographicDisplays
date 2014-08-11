package com.gmail.filoghost.holograms.tasks;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.google.common.collect.Maps;

public class WorldPlayerCounterTask implements Runnable {

	private static Map<String, Integer> worlds = Maps.newHashMap();
	
	@Override
	public void run() {
		worlds.clear();
		
		for (World world : Bukkit.getWorlds()) {
			worlds.put(world.getName().toLowerCase(), world.getPlayers().size());
		}
	}
	
	public static String getCount(String world) {
		Integer count = worlds.get(world);
		return count != null ? count.toString() : "world not found";
	}
}
