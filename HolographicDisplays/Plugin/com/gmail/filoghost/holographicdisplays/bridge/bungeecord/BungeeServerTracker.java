package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;

public class BungeeServerTracker {
	
	private static Map<String, BungeeServerInfo> trackedServers = new HashMap<String, BungeeServerInfo>();
	private static int taskID = -1;

	public static void resetTrackedServers() {
		trackedServers.clear();
	}
	
	public static void track(String server) {
		if (!trackedServers.containsKey(server)) {
			BungeeServerInfo info = new BungeeServerInfo(0, System.currentTimeMillis());
			trackedServers.put(server, info);
			BungeeChannel.getInstance().askPlayerCount(server);
		}
	}
	
	public static void untrack(String server) {
		trackedServers.remove(server);
	}
	
	// Handle a successful ping.
	protected static void handlePing(String server, int online) {
		BungeeServerInfo info = trackedServers.get(server);
		if (info == null) {
			info = new BungeeServerInfo(online, System.currentTimeMillis());
			trackedServers.put(server, info);
		} else {
			info.setOnlinePlayers(online);
		}
	}
	
	public static int getPlayersOnline(String server) {
		BungeeServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.setLastRequest(System.currentTimeMillis());
			return info.getOnlinePlayers();
		} else {
			// It was not tracked, add it.
			track(server);
			return 0;
		}
	}

	public static Map<String, BungeeServerInfo> getTrackedServers() {
		return trackedServers;
	}
	
	public static void startTask(int refreshSeconds) {
		
		if (taskID != -1) {
			Bukkit.getScheduler().cancelTask(taskID);
		}
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HolographicDisplays.getInstance(), new Runnable() {
			public void run() {
				for (String server : trackedServers.keySet()) {
					BungeeChannel.getInstance().askPlayerCount(server);
				}
			}
		}, 0, refreshSeconds * 20);
	}
}
