/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.bungeecord;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

public class BungeeMessenger implements PluginMessageListener {

    private static final String BUNGEECORD_CHANNEL = "BungeeCord";
    private static final String REDISBUNGEE_CHANNEL = "legacy:redisbungee";

    private final Plugin plugin;
    private final PlayerCountListener playerCountListener;

    private BungeeMessenger(Plugin plugin, PlayerCountListener playerCountListener) {
        this.plugin = plugin;
        this.playerCountListener = playerCountListener;
    }

    public static BungeeMessenger registerNew(Plugin plugin, PlayerCountListener playerCountListener) {
        BungeeMessenger bungeeMessenger = new BungeeMessenger(plugin, playerCountListener);

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, BUNGEECORD_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, BUNGEECORD_CHANNEL, bungeeMessenger);

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, REDISBUNGEE_CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, REDISBUNGEE_CHANNEL, bungeeMessenger);

        return bungeeMessenger;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (Settings.pingerEnabled || !channel.equals(getTargetChannel())) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subChannel = in.readUTF();
            if (!subChannel.equals("PlayerCount")) {
                return;
            }

            if (in.available() == 0) {
                // The server name was unknown on BungeeCord, can't know which one
                return;
            }

            String server = in.readUTF();
            int online = in.readInt();
            playerCountListener.onReceive(server, online);
        } catch (IOException e) {
            Log.warning("Error while decoding player count from BungeeCord.", e);
        }
    }

    public void sendPlayerCountRequest(String server) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteOut);

        try {
            out.writeUTF("PlayerCount");
            out.writeUTF(server);
        } catch (IOException e) {
            Log.warning("Error while encoding player count message for server \"" + server + "\".", e);
        }

        // Send the message through a random player (BungeeCord will not forward it to them)
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.size() > 0) {
            Player player = players.iterator().next();
            player.sendPluginMessage(plugin, getTargetChannel(), byteOut.toByteArray());
        }
    }

    private String getTargetChannel() {
        if (Settings.useRedisBungee) {
            return REDISBUNGEE_CHANNEL;
        } else {
            return BUNGEECORD_CHANNEL;
        }
    }


    public interface PlayerCountListener {

        void onReceive(String serverName, int playerCount);

    }

}
