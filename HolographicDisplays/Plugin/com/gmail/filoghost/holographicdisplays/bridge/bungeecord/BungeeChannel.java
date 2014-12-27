package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;

public class BungeeChannel implements PluginMessageListener {

	private static BungeeChannel instance;
	
	public static BungeeChannel getInstance() {
		if (instance == null) {
			instance = new BungeeChannel(HolographicDisplays.getInstance());
		}
		return instance;
	}
	
	public BungeeChannel(Plugin plugin) {
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "RedisBungee");
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "RedisBungee", this);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord") && !channel.equals("RedisBungee")) {
            return;
        }
		
		 DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		 
		 try {
			 String subChannel = in.readUTF();
			 
			 if (subChannel.equals("PlayerCount")) {
				 
				 String server = in.readUTF();
				 
				 if (in.available() > 0) {
					 int online = in.readInt();
					 BungeeServerTracker.handlePing(server, online);
				 }
			 }
		 
		} catch (EOFException e) {
			// Do nothing.
		} catch (IOException e) {
			// This should never happen.
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void askPlayerCount(String server) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try {
			out.writeUTF("PlayerCount");
			out.writeUTF(server);
		} catch (IOException e) {
			// It should not happen.
			e.printStackTrace();
			HolographicDisplays.getInstance().getLogger().warning("I/O Exception while asking for player count on server '" + server + "'.");
		}

		// OR, if you don't need to send it to a specific player
		Player[] players = Bukkit.getOnlinePlayers();
		if (players.length > 0) {
			players[0].sendPluginMessage(HolographicDisplays.getInstance(), Configuration.useRedisBungee ? "RedisBungee" : "BungeeCord", b.toByteArray());
		}
	}
}
