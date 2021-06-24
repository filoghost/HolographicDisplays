/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.bungeecord;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.pinger.PingResponse;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.pinger.ServerPinger;
import me.filoghost.holographicdisplays.plugin.disk.Settings;
import me.filoghost.holographicdisplays.plugin.disk.ServerAddress;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class BungeeServerTracker {

    private static final long UNTRACK_AFTER_TIME_WITHOUT_REQUESTS = TimeUnit.MINUTES.toMillis(10);

    private final ConcurrentMap<String, TrackedServer> trackedServers;
    private final BungeeMessenger bungeeMessenger;

    private int taskID = -1;

    public BungeeServerTracker(Plugin plugin) {
        trackedServers = new ConcurrentHashMap<>();
        bungeeMessenger = BungeeMessenger.registerNew(plugin, this::updateServerInfoFromBungee);
    }

    public void restart(int updateInterval, TimeUnit timeUnit) {
        trackedServers.clear();

        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
        }

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HolographicDisplays.getInstance(),
                this::runPeriodicUpdateTask, 1, timeUnit.toSeconds(updateInterval) * 20L);
    }

    public ServerInfo getCurrentServerInfo(@NotNull String serverName) {
        // If it wasn't already tracked, send an update request instantly
        if (!Settings.pingerEnabled && !trackedServers.containsKey(serverName)) {
            bungeeMessenger.sendPlayerCountRequest(serverName);
        }

        TrackedServer trackedServer = trackedServers.computeIfAbsent(serverName, TrackedServer::new);
        trackedServer.updateLastRequest();
        return trackedServer.serverInfo;
    }

    private void runPeriodicUpdateTask() {
        removeUnusedServers();

        if (Settings.pingerEnabled) {
            Bukkit.getScheduler().runTaskAsynchronously(HolographicDisplays.getInstance(), () -> {
                for (TrackedServer trackedServer : trackedServers.values()) {
                    updateServerInfoWithPinger(trackedServer);
                }
            });
        } else {
            for (String serverName : trackedServers.keySet()) {
                bungeeMessenger.sendPlayerCountRequest(serverName);
            }
        }
    }

    private void updateServerInfoWithPinger(TrackedServer trackedServer) {
        ServerAddress serverAddress = Settings.pingerServerAddresses.get(trackedServer.serverName);

        if (serverAddress != null) {
            trackedServer.serverInfo = pingServer(serverAddress);
        } else {
            trackedServer.serverInfo = ServerInfo.offline("[Unknown server: " + trackedServer.serverName + "]");
        }
    }

    private void updateServerInfoFromBungee(String serverName, int onlinePlayers) {
        TrackedServer trackedServer = trackedServers.get(serverName);
        if (trackedServer != null) {
            trackedServer.serverInfo = ServerInfo.online(onlinePlayers, 0, "");
        }
    }

    private ServerInfo pingServer(ServerAddress serverAddress) {
        try {
            PingResponse data = ServerPinger.fetchData(serverAddress, Settings.pingerTimeout);
            return ServerInfo.online(data.getOnlinePlayers(), data.getMaxPlayers(), data.getMotd());
        } catch (SocketTimeoutException e) {
            // Common error, do not log
        } catch (UnknownHostException e) {
            Log.warning("Couldn't fetch data from " + serverAddress + ": unknown host address.");
        } catch (IOException e) {
            Log.warning("Couldn't fetch data from " + serverAddress + ".", e);
        }

        return ServerInfo.offline(Settings.pingerOfflineMotd);
    }

    private void removeUnusedServers() {
        long now = System.currentTimeMillis();

        trackedServers.values().removeIf(trackedServer -> {
            if (now - trackedServer.lastRequest > UNTRACK_AFTER_TIME_WITHOUT_REQUESTS) {
                DebugLogger.info("Untracked unused server \"" + trackedServer.serverName + "\".");
                return true;
            } else {
                return false;
            }
        });
    }

    private static class TrackedServer {

        private final String serverName;
        private volatile ServerInfo serverInfo;
        private volatile long lastRequest;

        private TrackedServer(String serverName) {
            this.serverName = serverName;
            this.serverInfo = ServerInfo.offline(Settings.pingerOfflineMotd);
        }

        private void updateLastRequest() {
            this.lastRequest = System.currentTimeMillis();
        }

    }

}
