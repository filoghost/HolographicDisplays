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
package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger.PingResponse;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger.ServerPinger;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.disk.ServerAddress;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;

public class BungeeServerTracker {
	
	private static Map<String, BungeeServerInfo> trackedServers = new ConcurrentHashMap<String, BungeeServerInfo>();
	private static int taskID = -1;
	
	public static void resetTrackedServers() {
		trackedServers.clear();
	}
	
	public static void track(String server) {
		if (!trackedServers.containsKey(server)) {
			BungeeServerInfo info = new BungeeServerInfo();
			info.setMotd(Configuration.pingerOfflineMotd);
			trackedServers.put(server, info);
			
			if (!Configuration.pingerEnable) {
				BungeeChannel.getInstance().askPlayerCount(server);
			}
		}
	}
	
	public static void untrack(String server) {
		trackedServers.remove(server);
	}
	
	protected static BungeeServerInfo getOrCreateServerInfo(String server) {
		BungeeServerInfo info = trackedServers.get(server);
		if (info == null) {
			info = new BungeeServerInfo();
			info.setMotd(Configuration.pingerOfflineMotd);
			trackedServers.put(server, info);
		}
		
		return info;
	}

	public static int getPlayersOnline(String server) {
		BungeeServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.updateLastRequest();
			return info.getOnlinePlayers();
		} else {
			// It was not tracked, add it.
			track(server);
			return 0;
		}
	}
	
	public static String getMaxPlayers(String server) {
		
		if (!Configuration.pingerEnable) {
			return "[Please enable pinger]";
		}
		
		BungeeServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.updateLastRequest();
			return String.valueOf(info.getMaxPlayers());
		} else {
			// It was not tracked, add it.
			track(server);
			return "0";
		}
	}
	
	public static String getMotd1(String server) {
		
		if (!Configuration.pingerEnable) {
			return "[Please enable pinger]";
		}
		
		BungeeServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.updateLastRequest();
			return info.getMotd1();
		} else {
			// It was not tracked, add it.
			track(server);
			return Configuration.pingerOfflineMotd;
		}
	}
	
	public static String getMotd2(String server) {
		
		if (!Configuration.pingerEnable) {
			return "[Please enable pinger]";
		}
		
		BungeeServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.updateLastRequest();
			return info.getMotd2();
		} else {
			// It was not tracked, add it.
			track(server);
			return "";
		}
	}
	
	public static String getOnlineStatus(String server) {
		
		if (!Configuration.pingerEnable) {
			return "[Please enable pinger]";
		}
		
		BungeeServerInfo info = trackedServers.get(server);
		if (info != null) {
			info.updateLastRequest();
			return info.isOnline() ? Configuration.pingerStatusOnline : Configuration.pingerStatusOffline;
		} else {
			// It was not tracked, add it.
			track(server);
			return "0";
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
			
			@Override
			public void run() {
				
				if (Configuration.pingerEnable) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							for (Entry<String, ServerAddress> entry : Configuration.pingerServers.entrySet()) {
								
								BungeeServerInfo serverInfo = getOrCreateServerInfo(entry.getKey());
								boolean displayOffline = false;
								
								try {
									PingResponse data = ServerPinger.fetchData(entry.getValue(), Configuration.pingerTimeout);
									
									if (data.isOnline()) {
										serverInfo.setOnline(true);
										serverInfo.setOnlinePlayers(data.getOnlinePlayers());
										serverInfo.setMaxPlayers(data.getMaxPlayers());
										serverInfo.setMotd(data.getMotd());
									} else {
										displayOffline = true;
									}
								} catch (SocketTimeoutException e) {
									displayOffline = true;
								} catch (UnknownHostException e) {
									ConsoleLogger.log(Level.WARNING, "Couldn't fetch data from " + entry.getKey() + "(" + entry.getValue().toString() + "): unknown host address.");
									displayOffline = true;
								} catch (IOException e) {
									displayOffline = true;
								} catch (Throwable t) {
									displayOffline = true;
									ConsoleLogger.log(Level.WARNING, "Couldn't fetch data from " + entry.getKey() + "(" + entry.getValue().toString() + ")", t);
								}
								
								if (displayOffline) {
									serverInfo.setOnline(false);
									serverInfo.setOnlinePlayers(0);
									serverInfo.setMaxPlayers(0);
									serverInfo.setMotd(Configuration.pingerOfflineMotd);
								}
							}
						}
					}.runTaskAsynchronously(HolographicDisplays.getInstance());
					
				} else {
					for (String server : trackedServers.keySet()) {
						BungeeChannel.getInstance().askPlayerCount(server);
					}
				}
				
			}
		}, 1, refreshSeconds * 20);
	}
}
