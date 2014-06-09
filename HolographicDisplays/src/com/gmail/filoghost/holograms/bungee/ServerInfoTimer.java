package com.gmail.filoghost.holograms.bungee;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.gmail.filoghost.holograms.HolographicDisplays;

public class ServerInfoTimer {
	
	private static Map<String, ServerInfo> trackedServers = new HashMap<String, ServerInfo>();
	private static int taskID = -1;
	private static int refreshSeconds = 3;

	public static void resetTrackedServers() {
		trackedServers.clear();
	}
	
	public static void track(String server) {
		if (!trackedServers.containsKey(server)) {
			ServerInfo info = new ServerInfo(0, false, 0);
			info.setLastRequest(System.currentTimeMillis());
			trackedServers.put(server, info);
			BungeeChannel.instance().askPlayerCount(server);
		}
	}
	
	public static void untrack(String server) {
		trackedServers.remove(server);
	}
	
	public static void setRefreshSeconds(int seconds) {
		if (seconds < 1) {
			refreshSeconds = 1;
		} else {
			refreshSeconds = seconds;
		}
	}
	
	// Handle a successful ping.
	public static void handlePing(String server, int online) {
		ServerInfo info = trackedServers.get(server);
		if (info == null) {
			info = new ServerInfo(online, true, System.currentTimeMillis());
			trackedServers.put(server, info);
		} else {
			info.setLastPing(System.currentTimeMillis());
			info.setOnline(true);
			info.setOnlinePlayers(online);
		}
	}
	
	public static void handleOffline(String server) {
		ServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.setOnlinePlayers(0);
			info.setOnline(false);
		}
	}
	
	public static int getPlayersOnline(String server) {
		ServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.setLastRequest(System.currentTimeMillis());
			return info.getOnlinePlayers();
		} else {
			// It was not tracked, add it.
			track(server);
			return 0;
		}
	}
	
	public static boolean getOnlineStatus(String server) {
		ServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.setLastRequest(System.currentTimeMillis());
			return info.isOnline();
		} else {
			// It was not tracked, add it.
			track(server);
			return false;
		}
	}
	
	public static Map<String, ServerInfo> getMap() {
		return trackedServers;
	}
	
	public static void startTask() {
		
		if (taskID != -1) {
			Bukkit.getScheduler().cancelTask(taskID);
		}
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HolographicDisplays.getInstance(), new Runnable() {
			public void run() {
				for (String server : trackedServers.keySet()) {
					BungeeChannel.instance().askPlayerCount(server);
				}
			}
		}, 0, refreshSeconds * 20);
	}
}
