/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.bungeecord;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.bridge.bungeecord.serverpinger.PingResponse;
import me.filoghost.holographicdisplays.bridge.bungeecord.serverpinger.ServerPinger;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.disk.ServerAddress;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BungeeServerTracker {

    private static final String PINGER_NOT_ENABLED_ERROR = "[Please enable pinger]";

    private final BungeeChannel bungeeChannel;
    private final Map<String, BungeeServerInfo> trackedServers;
    
    private int taskID = -1;

    public BungeeServerTracker(Plugin plugin) {
        bungeeChannel = new BungeeChannel(this);
        bungeeChannel.register(plugin);
        trackedServers = new ConcurrentHashMap<>();
    }

    public void resetTrackedServers() {
        trackedServers.clear();
    }
    
    public void track(String server) {
        if (!trackedServers.containsKey(server)) {
            BungeeServerInfo info = new BungeeServerInfo();
            info.setMotd(Configuration.pingerOfflineMotd);
            trackedServers.put(server, info);
            
            if (!Configuration.pingerEnabled) {
                bungeeChannel.askPlayerCount(server);
            }
        }
    }
    
    protected BungeeServerInfo getOrCreateServerInfo(String server) {
        BungeeServerInfo info = trackedServers.get(server);
        if (info == null) {
            info = new BungeeServerInfo();
            info.setMotd(Configuration.pingerOfflineMotd);
            trackedServers.put(server, info);
        }
        
        return info;
    }

    public int getPlayersOnline(String server) {
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
    
    public String getMaxPlayers(String server) {
        if (!Configuration.pingerEnabled) {
            return PINGER_NOT_ENABLED_ERROR;
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
    
    public String getMotd1(String server) {
        if (!Configuration.pingerEnabled) {
            return PINGER_NOT_ENABLED_ERROR;
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
    
    public String getMotd2(String server) {
        if (!Configuration.pingerEnabled) {
            return PINGER_NOT_ENABLED_ERROR;
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
    
    public String getOnlineStatus(String server) {
        if (!Configuration.pingerEnabled) {
            return PINGER_NOT_ENABLED_ERROR;
        }
        
        BungeeServerInfo info = trackedServers.get(server);
        if (info != null) {
            info.updateLastRequest();
            return info.isOnline() ? Configuration.pingerStatusOnline : Configuration.pingerStatusOffline;
        } else {
            // It was not tracked, add it.
            track(server);
            return Configuration.pingerStatusOffline;
        }
    }

    public Map<String, BungeeServerInfo> getTrackedServers() {
        return trackedServers;
    }
    
    public void restartTask(int refreshSeconds) {
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HolographicDisplays.getInstance(), () -> {
            if (Configuration.pingerEnabled) {
                runAsyncPinger();
            } else {
                for (String server : trackedServers.keySet()) {
                    bungeeChannel.askPlayerCount(server);
                }
            }

        }, 1, refreshSeconds * 20L);
    }

    private void runAsyncPinger() {
        Bukkit.getScheduler().runTaskAsynchronously(HolographicDisplays.getInstance(), () -> {
            for (ServerAddress serverAddress : Configuration.pingerServers) {
                BungeeServerInfo serverInfo = getOrCreateServerInfo(serverAddress.getName());
                boolean displayOffline = false;
                
                try {
                    PingResponse data = ServerPinger.fetchData(serverAddress, Configuration.pingerTimeout);
                    
                    if (data.isOnline()) {
                        serverInfo.setOnline(true);
                        serverInfo.setOnlinePlayers(data.getOnlinePlayers());
                        serverInfo.setMaxPlayers(data.getMaxPlayers());
                        serverInfo.setMotd(data.getMotd());
                    } else {
                        displayOffline = true;
                    }
                } catch (SocketTimeoutException e) {
                    // Common error, avoid logging
                    displayOffline = true;
                } catch (UnknownHostException e) {
                    Log.warning("Couldn't fetch data from " + serverAddress + ": unknown host address.");
                    displayOffline = true;
                } catch (IOException e) {
                    Log.warning("Couldn't fetch data from " + serverAddress + ".", e);
                    displayOffline = true;
                }
                
                if (displayOffline) {
                    serverInfo.setOnline(false);
                    serverInfo.setOnlinePlayers(0);
                    serverInfo.setMaxPlayers(0);
                    serverInfo.setMotd(Configuration.pingerOfflineMotd);
                }
            }
        });
    }

}
