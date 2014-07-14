package com.gmail.filoghost.holograms.bungee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.gmail.filoghost.holograms.HolographicDisplays;

public class BungeeChannel implements PluginMessageListener {

	private static BungeeChannel instance;
	
	private BungeeChannel() { }
	
	private static void initialize() {
		instance = new BungeeChannel();
		Bukkit.getMessenger().registerOutgoingPluginChannel(HolographicDisplays.getInstance(), "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(HolographicDisplays.getInstance(), "BungeeCord", instance);
	}
	
	public static BungeeChannel instance() {
		if (instance == null) {
			initialize();
		}
		return instance;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
            return;
        }
		
		 DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		 
		 try {
			 String subChannel = in.readUTF();			 
			 
			 if (subChannel.equals("PlayerCount")) {
				 
				 String server = in.readUTF();
				 
				 if (in.available() > 0) {
					 int online = in.readInt();
					 ServerInfoTimer.handlePing(server, online);
				 } else {
					 // If there are no bytes available it means that the server is offline.
					 ServerInfoTimer.handleOffline(server);
				 }
			 }
		 
		} catch (EOFException e) {
			// Do nothing.
		} catch (IOException e) {
			// This should never happen.
			e.printStackTrace();
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
			HolographicDisplays.getInstance().getLogger().warning("I/O Exception while asking for player count on server '" + server + "'.");
		}

		// OR, if you don't need to send it to a specific player
		Player[] players = Bukkit.getOnlinePlayers();
		if (players.length > 0) {
			players[0].sendPluginMessage(HolographicDisplays.getInstance(), "BungeeCord", b.toByteArray());
		}
	}
}
