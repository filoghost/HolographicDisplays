package com.gmail.filoghost.holograms.utils;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.bungee.ServerInfoTimer;
import com.gmail.filoghost.holograms.bungee.ServerInfo;

public class BungeeCleanupTask {

	private static int taskID = -1;
	
	public static void start() {
		if (taskID != -1) {
			Bukkit.getScheduler().cancelTask(taskID);
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(HolographicDisplays.getInstance(), new Runnable() {
			
			public void run() {
				
				Iterator<Entry<String, ServerInfo>> iter = ServerInfoTimer.getMap().entrySet().iterator();
				
				while (iter.hasNext()) {					
					long lastRequest = iter.next().getValue().getLastRequest();
					
					if (lastRequest != 0 && System.currentTimeMillis() - lastRequest > 600000) { // 10 * 60 * 1000 = 10 minutes.
						// Don't track that server anymore.
						iter.remove();
					}
				}
			}
			
		}, 5 * 60 * 20, 5 * 60 * 20); // Every 5 minutes.
	}
	
	

}
