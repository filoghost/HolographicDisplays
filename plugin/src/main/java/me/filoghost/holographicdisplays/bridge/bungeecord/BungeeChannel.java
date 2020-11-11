/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.bungeecord;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.common.ConsoleLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

public class BungeeChannel implements PluginMessageListener {
    
    private static final String BUNGEECORD_CHANNEL = "BungeeCord";
    private static final String REDISBUNGEE_CHANNEL = "legacy:redisbungee";

    private static BungeeChannel instance;

    public static BungeeChannel getInstance() {
        if (instance == null) {
            instance = new BungeeChannel(HolographicDisplays.getInstance());
        }
        return instance;
    }

    private BungeeChannel(Plugin plugin) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, BUNGEECORD_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, BUNGEECORD_CHANNEL, this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, REDISBUNGEE_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, REDISBUNGEE_CHANNEL, this);
    }
    
    private String getTargetChannel() {
        if (Configuration.useRedisBungee) {
            return REDISBUNGEE_CHANNEL;
        } else {
            return BUNGEECORD_CHANNEL;
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(getTargetChannel())) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

            try {
                String subChannel = in.readUTF();

                if (subChannel.equals("PlayerCount")) {
                    String server = in.readUTF();

                    if (in.available() > 0) {
                        int online = in.readInt();

                        BungeeServerInfo serverInfo = BungeeServerTracker.getOrCreateServerInfo(server);
                        serverInfo.setOnlinePlayers(online);
                    }
                }

            } catch (EOFException e) {
                // Do nothing.
            } catch (IOException e) {
                // This should never happen.
                e.printStackTrace();
            }
        }
    }

    public void askPlayerCount(String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("PlayerCount");
            out.writeUTF(server);
        } catch (IOException e) {
            // It should not happen.
            ConsoleLogger.log(Level.WARNING, "I/O Exception while asking for player count on server '" + server + "'.", e);
        }

        // OR, if you don't need to send it to a specific player
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.size() > 0) {
            players.iterator().next().sendPluginMessage(HolographicDisplays.getInstance(), getTargetChannel(), b.toByteArray());
        }
    }
}
