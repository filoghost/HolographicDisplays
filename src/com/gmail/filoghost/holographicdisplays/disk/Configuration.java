package com.gmail.filoghost.holographicdisplays.disk;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger.ServerAddress;
import com.gmail.filoghost.holographicdisplays.util.Utils;

/**
 * Just a bunch of static varibles to hold the settings.
 * Useful for fast access.
 */
public class Configuration {
	
	public static double spaceBetweenLines;
	public static String imageSymbol;
	public static String transparencySymbol;
	public static boolean updateNotification;
	public static ChatColor transparencyColor;
	
	public static SimpleDateFormat timeFormat;
	
	public static int bungeeRefreshSeconds;
	public static boolean useRedisBungee;
	
	public static boolean pingerEnable;
	public static int pingerTimeout;
	public static String pingerOfflineMotd;
	public static String pingerStatusOnline;
	public static String pingerStatusOffline;
	public static boolean pingerTrimMotd;
	public static Map<String, ServerAddress> pingerServers;
	
	public static boolean debug;
	
	
	public static void load(Plugin plugin) {
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			plugin.getDataFolder().mkdirs();
			plugin.saveResource("config.yml", true);
		}
		
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			plugin.getLogger().warning("The configuration is not a valid YAML file! Please check it with a tool like http://yaml-online-parser.appspot.com/");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().warning("I/O error while reading the configuration. Was the file in use?");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			plugin.getLogger().warning("Unhandled exception while reading the configuration!");
			return;
		}
		
		boolean needsSave = false;
		
		for (ConfigNode node : ConfigNode.values()) {
			if (!config.isSet(node.getPath())) {
				needsSave = true;
				config.set(node.getPath(), node.getDefaultValue());
			}
		}
		
		// Check the old values.
		List<String> nodesToRemove = Arrays.asList(
				"vertical-spacing",
				"time-format",
				"bungee-refresh-seconds",
				"using-RedisBungee",
				"bungee-online-format",
				"bungee-offline-format"
				);

		for (String oldNode : nodesToRemove) {
			if (config.isSet(oldNode)) {
				config.set(oldNode, null);
				needsSave = true;
			}
		}
		
		
		
		if (needsSave) {
			config.options().header(Utils.join(new String[] {
					".",
					".  Read the tutorial at: http://dev.bukkit.org/bukkit-plugins/holographic-displays/",
					".",
					".  Plugin created by filoghost.",
					"."},
					"\n"));
			config.options().copyHeader(true);
			try {
				config.save(configFile);
			} catch (IOException e) {
				e.printStackTrace();
				plugin.getLogger().warning("I/O error while saving the configuration. Was the file in use?");
			}
		}
		
		spaceBetweenLines = config.getDouble(ConfigNode.SPACE_BETWEEN_LINES.getPath());
		updateNotification = config.getBoolean(ConfigNode.UPDATE_NOTIFICATION.getPath());
		
		imageSymbol = StringConverter.toReadableFormat(config.getString(ConfigNode.IMAGES_SYMBOL.getPath()));
		transparencySymbol = StringConverter.toReadableFormat(config.getString(ConfigNode.TRANSPARENCY_SPACE.getPath()));
		bungeeRefreshSeconds = config.getInt(ConfigNode.BUNGEE_REFRESH_SECONDS.getPath());
		useRedisBungee = config.getBoolean(ConfigNode.BUNGEE_USE_REDIS_BUNGEE.getPath());
		
		pingerEnable = config.getBoolean(ConfigNode.BUNGEE_USE_FULL_PINGER.getPath());
		pingerTimeout = config.getInt(ConfigNode.BUNGEE_PINGER_TIMEOUT.getPath());
		pingerTrimMotd = config.getBoolean(ConfigNode.BUNGEE_PINGER_TRIM_MOTD.getPath());
		
		pingerOfflineMotd = StringConverter.toReadableFormat(config.getString(ConfigNode.BUNGEE_PINGER_OFFLINE_MOTD.getPath()));
		pingerStatusOnline = StringConverter.toReadableFormat(config.getString(ConfigNode.BUNGEE_PINGER_ONLINE_FORMAT.getPath()));
		pingerStatusOffline = StringConverter.toReadableFormat(config.getString(ConfigNode.BUNGEE_PINGER_OFFLINE_FORMAT.getPath()));
		
		if (pingerTimeout <= 0) {
			pingerTimeout = 100;
		} else if (pingerTimeout >= 10000) {
			pingerTimeout = 10000;
		}
		
		pingerServers = Utils.newMap();
		
		if (pingerEnable) {
			for (String singleServer : config.getStringList(ConfigNode.BUNGEE_PINGER_SERVERS.getPath())) {
				String[] nameAndAddress = singleServer.split(":", 2);
				if (nameAndAddress.length < 2) {
					plugin.getLogger().warning("The server info \"" + singleServer + "\" is not valid. There should be a name and an address, separated by a colon.");
					continue;
				}
				
				String name = nameAndAddress[0].trim();
				String address = nameAndAddress[1].replace(" ", "");
				
				String ip;
				int port;
				
				if (address.contains(":")) {
					String[] ipAndPort = address.split(":", 2);
					ip = ipAndPort[0];
					try {
						port = Integer.parseInt(ipAndPort[1]);
					} catch (NumberFormatException e) {
						plugin.getLogger().warning("Invalid port number in the server info \"" + singleServer + "\".");
						continue;
					}
				} else {
					ip = address;
					port = 25565; // The default Minecraft port.
				}
				
				pingerServers.put(name, new ServerAddress(ip, port));
			}
		}
		
		debug = config.getBoolean(ConfigNode.DEBUG.getPath());
		
		String tempColor = config.getString(ConfigNode.TRANSPARENCY_COLOR.getPath()).replace('&', ChatColor.COLOR_CHAR);
		boolean foundColor = false;
		for (ChatColor chatColor : ChatColor.values()) {
			if (chatColor.toString().equals(tempColor)) {
				Configuration.transparencyColor = chatColor;
				foundColor = true;
			}
		}
		if (!foundColor) {
			Configuration.transparencyColor = ChatColor.GRAY;
			plugin.getLogger().warning("You didn't set a valid chat color for transparency in the configuration, light gray (&7) will be used.");
		}
		
		try {
			timeFormat = new SimpleDateFormat(config.getString(ConfigNode.TIME_FORMAT.getPath()));
			timeFormat.setTimeZone(TimeZone.getTimeZone(config.getString(ConfigNode.TIME_ZONE.getPath())));
		} catch (IllegalArgumentException ex) {
			timeFormat = new SimpleDateFormat("H:mm");
			plugin.getLogger().warning("Time format not valid in the configuration, using the default.");
		}
		
		if (bungeeRefreshSeconds < 1) {
			plugin.getLogger().warning("The minimum interval for pinging BungeeCord's servers is 1 second. It has been automatically set.");
			bungeeRefreshSeconds = 1;
		}
		
		if (bungeeRefreshSeconds > 60) {
			plugin.getLogger().warning("The maximum interval for pinging BungeeCord's servers is 60 seconds. It has been automatically set.");
			bungeeRefreshSeconds = 60;
		}
	}
}
