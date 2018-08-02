package com.gmail.filoghost.holographicdisplays.bridge.bungeecord;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.Collection;

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
		if (BukkitVersion.isAtLeast(BukkitVersion.v1_13_R1)) {
			// TODO: implement 1.13 channel when RedisBungee will be updated
		} else {
			Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "RedisBungee");
			Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "RedisBungee", this);
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		switch (channel) {
			case "BungeeCord":
				if (Configuration.useRedisBungee) {
					// If we use RedisBungee, we must ignore this channel.
					return;
				}
				break;
			case "RedisBungee":
				if (!Configuration.useRedisBungee) {
					// Same as above, just the opposite case.
					return;
				}
				break;
			default:
				// Not our channels, ignore the message.
				return;
		}
		try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
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
			ConsoleLogger.error("Can't read bungeecord message.", e);
		}
	}

	public void askPlayerCount(String server) {
		try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			 DataOutputStream out = new DataOutputStream(byteOut)) {
			out.writeUTF("PlayerCount");
			out.writeUTF(server);
			Collection<? extends Player> players = BukkitUtils.getOnlinePlayers();
			if (players.size() > 0) {
				players.iterator().next().sendPluginMessage(HolographicDisplays.getInstance(), Configuration.useRedisBungee ? "RedisBungee" : "BungeeCord", byteOut.toByteArray());
			}
		} catch (IOException e) {
			// It should not happen.
			ConsoleLogger.error("I/O Exception while asking for player count on server '" + server + "'.", e);
		}
	}
}
