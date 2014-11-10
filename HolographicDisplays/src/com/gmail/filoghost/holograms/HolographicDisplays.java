package com.gmail.filoghost.holograms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holograms.bungee.ServerInfoTimer;
import com.gmail.filoghost.holograms.commands.main.HologramsCommandHandler;
import com.gmail.filoghost.holograms.database.HologramDatabase;
import com.gmail.filoghost.holograms.exception.HologramNotFoundException;
import com.gmail.filoghost.holograms.exception.InvalidLocationException;
import com.gmail.filoghost.holograms.exception.WorldNotFoundException;
import com.gmail.filoghost.holograms.listener.MainListener;
import com.gmail.filoghost.holograms.metrics.MetricsLite;
import com.gmail.filoghost.holograms.nms.interfaces.NmsManager;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.placeholders.AnimationManager;
import com.gmail.filoghost.holograms.placeholders.PlaceholderManager;
import com.gmail.filoghost.holograms.placeholders.StaticPlaceholders;
import com.gmail.filoghost.holograms.protocol.ProtocolLibHook;
import com.gmail.filoghost.holograms.tasks.BungeeCleanupTask;
import com.gmail.filoghost.holograms.tasks.WorldPlayerCounterTask;
import com.gmail.filoghost.holograms.utils.StringUtils;
import com.gmail.filoghost.holograms.utils.VersionUtils;
import com.gmail.filoghost.holograms.utils.ConfigNode;
import com.gmail.filoghost.holograms.SimpleUpdater.ResponseHandler;

public class HolographicDisplays extends JavaPlugin {

	private static Logger logger;
	private static HolographicDisplays instance;
	
	public static NmsManager nmsManager;
	private HologramsCommandHandler mainCommandHandler;
	private static PlaceholderManager placeholderManager;
	
	public void onEnable() {
		
		if (instance != null) {
			getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/hd reload\" instead.");
			return;
		}		
		
		instance = this;
		logger = getLogger();
		
		try {
			File oldItemDb = new File(getDataFolder(), "database-items.yml");
			if (oldItemDb.exists()) {
				oldItemDb.delete();
				logger.info("Deleted old database-items.yml file.");
			}
		} catch	(Exception e) {
			
		}
		
		// Load placeholders.yml.
		try {
			StaticPlaceholders.load();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Unable to read placeholders.yml! Is the file in use?");
		}

		// Load the configuration.
		loadConfiguration();
		
		if (Configuration.updateNotification) {
			new SimpleUpdater(this, 75097).checkForUpdates(new ResponseHandler() {
				
				@Override
				public void onUpdateFound(final String newVersion) {

					Configuration.newVersion = newVersion;
					logger.info("Found a new version available: " + newVersion);
					logger.info("Download it on Bukkit Dev:");
					logger.info("dev.bukkit.org/bukkit-plugins/holographic-displays");					
				}
			});
		}
		
		String version = VersionUtils.getBukkitVersion();
		
		if (version == null) {
			// Caused by MCPC+ / Cauldron renaming packages, extract the version from Bukkit.getVersion()
			version = VersionUtils.getMinecraftVersion();
			
			if ("1.6.4".equals(version)) {
				version = "v1_6_R3";
			} else if ("1.7.2".equals(version)) {
				version = "v1_7_R1";
			} else if ("1.7.5".equals(version)) {
				version = "v1_7_R2";
			} else if ("1.7.8".equals(version)) {
				version = "v1_7_R3";
			} else if ("1.7.10".equals(version)) {
				version = "v1_7_R4";
			} else {
				// Cannot definitely get the version. This will cause HD to disable itself.
				version = null;
			}
		}
		
		// It's simple, we don't need reflection.
		if ("v1_6_R3".equals(version)) {
			nmsManager = new com.gmail.filoghost.holograms.nms.v1_6_R3.NmsManagerImpl();
		} else if ("v1_7_R1".equals(version)) {
			nmsManager = new com.gmail.filoghost.holograms.nms.v1_7_R1.NmsManagerImpl();
		} else if ("v1_7_R2".equals(version)) {
			nmsManager = new com.gmail.filoghost.holograms.nms.v1_7_R2.NmsManagerImpl();
		} else if ("v1_7_R3".equals(version)) {
			nmsManager = new com.gmail.filoghost.holograms.nms.v1_7_R3.NmsManagerImpl();
		} else if ("v1_7_R4".equals(version)) {
			nmsManager = new com.gmail.filoghost.holograms.nms.v1_7_R4.NmsManagerImpl();
		} else {
			printWarnAndDisable(
				"******************************************************",
				"     This version of HolographicDisplays can",
				"     only work on these server versions:",
				"     1.6.4, from 1.7.2 to 1.7.10.",
				"     The plugin will be disabled.",
				"******************************************************"
			);
			return;
		}

		try {
			if (VersionUtils.isMCPCOrCauldron()) {
				getLogger().info("Trying to enable Cauldron/MCPC+ support...");
			}
			
			nmsManager.registerCustomEntities();
			
			if (VersionUtils.isMCPCOrCauldron()) {
				getLogger().info("Successfully added support for Cauldron/MCPC+!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			printWarnAndDisable(
				"******************************************************",
				"     HolographicDisplays was unable to register",
				"     custom entities, the plugin will be disabled.",
				"     Are you using the correct Bukkit version?",
				"******************************************************"
			);
			return;
		}
		
		// ProtocolLib check.
		try {
			if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
				ProtocolLibHook.initialize();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning("Failed to load ProtocolLib support. Is it updated?");
		}
		
		// Load animation files.
		try {
			AnimationManager.loadAnimations();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning("Failed to load animation files!");
		}
		
		// Instantiate a PlaceholderManager.
		placeholderManager = new PlaceholderManager();
		
		// Initalize other static classes.
		HologramDatabase.initialize();
		ServerInfoTimer.setRefreshSeconds(Configuration.bungeeRefreshSeconds);
		ServerInfoTimer.startTask();
		BungeeCleanupTask.start();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorldPlayerCounterTask(), 0L, 3 * 20L);
		
		Set<String> savedHolograms = HologramDatabase.getHolograms();
		if (savedHolograms != null && savedHolograms.size() > 0) {
			for (String singleSavedHologram : savedHolograms) {
				try {
					CraftHologram singleHologramEntity = HologramDatabase.loadHologram(singleSavedHologram);
					HologramManager.addHologram(singleHologramEntity);
				} catch (HologramNotFoundException e) {
					logger.warning("Hologram '" + singleSavedHologram + "' not found, skipping it.");
				} catch (InvalidLocationException e) {
					logger.warning("Hologram '" + singleSavedHologram + "' has an invalid location format.");
				} catch (WorldNotFoundException e) {
					logger.warning("Hologram '" + singleSavedHologram + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
				} catch (Exception e) {
					e.printStackTrace();
					logger.warning("Unhandled exception while loading '" + singleSavedHologram + "'. Please contact the developer.");
				}
			}
		}
		
		getCommand("holograms").setExecutor(mainCommandHandler = new HologramsCommandHandler());
		Bukkit.getPluginManager().registerEvents(new MainListener(nmsManager), this);
		
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (Exception ignore) { }
		
		// The entities are loaded when the server is ready.
		new BukkitRunnable() {
			public void run() {

				for (CraftHologram hologram : HologramManager.getHolograms()) {
					if (!hologram.update()) {
						logger.warning("Unable to spawn entities for the hologram '" + hologram.getName() + "'.");
					}
				}
			}
		}.runTaskLater(this, 10L);
	}
	
	public void loadConfiguration() {
		saveDefaultConfig();
		boolean needsSave = false;
		for (ConfigNode node : ConfigNode.values()) {
			if (!getConfig().isSet(node.getPath())) {
				getConfig().set(node.getPath(), node.getDefault());
				needsSave = true;
			}
		}
		if (needsSave) {
			getConfig().options().header(".\n"
									 + ".  Read the tutorial at: http://dev.bukkit.org/bukkit-plugins/holographic-displays/\n"
									 + ".\n"
									 + ".  Plugin created by filoghost.\n"
									 + ".");
			getConfig().options().copyHeader(true);
			saveConfig();
		}
		
		Configuration.updateNotification = ConfigNode.UPDATE_NOTIFICATION.getBoolean(getConfig());
		Configuration.verticalLineSpacing = ConfigNode.VERTICAL_SPACING.getDouble(getConfig());
		Configuration.imageSymbol = StringUtils.toReadableFormat(ConfigNode.IMAGES_SYMBOL.getString(getConfig()));		
		Configuration.transparencySymbol = StringUtils.toReadableFormat(ConfigNode.TRANSPARENCY_SPACE.getString(getConfig()));
		Configuration.bungeeRefreshSeconds = ConfigNode.BUNGEE_REFRESH_SECONDS.getInt(getConfig());
		Configuration.bungeeOnlineFormat = StringUtils.toReadableFormat(ConfigNode.BUNGEE_ONLINE_FORMAT.getString(getConfig()));
		Configuration.bungeeOfflineFormat = StringUtils.toReadableFormat(ConfigNode.BUNGEE_OFFLINE_FORMAT.getString(getConfig()));
		Configuration.redisBungee = ConfigNode.BUNGEE_USE_REDIS_BUNGEE.getBoolean(getConfig());
		
		try {
			Configuration.timeFormat = new SimpleDateFormat(StringUtils.toReadableFormat(ConfigNode.TIME_FORMAT.getString(getConfig())));
		} catch (IllegalArgumentException ex) {
			Configuration.timeFormat = new SimpleDateFormat("H:mm");
			logger.warning("Time format not valid, using the default.");
		}
		
		if (Configuration.bungeeRefreshSeconds < 1) {
			logger.warning("The minimum interval for pinging BungeeCord's servers is 1 second. It has been automatically set.");
			Configuration.bungeeRefreshSeconds = 1;
		}
		
		if (Configuration.bungeeRefreshSeconds > 30) {
			logger.warning("The maximum interval for pinging BungeeCord's servers is 30 seconds. It has been automatically set.");
			Configuration.bungeeRefreshSeconds = 30;
		}
		
		
		String tempColor = ConfigNode.TRANSPARENCY_COLOR.getString(getConfig()).replace("&", "§");
		boolean foundColor = false;
		for (ChatColor chatColor : ChatColor.values()) {
			if (chatColor.toString().equals(tempColor)) {
				Configuration.transparencyColor = chatColor;
				foundColor = true;
			}
		}
		if (!foundColor) {
			Configuration.transparencyColor = ChatColor.GRAY;
			logger.warning("You didn't set a valid chat color for the transparency, light gray will be used.");
		}
	}

	public void onDisable() {
		for (CraftHologram hologram : HologramManager.getHolograms()) {
			hologram.hide();
		}
	}
	
	private static void printWarnAndDisable(String... messages) {
		StringBuffer buffer = new StringBuffer("\n ");
		for (String message : messages) {
			buffer.append('\n');
			buffer.append(message);
		}
		buffer.append('\n');
		System.out.println(buffer.toString());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) { }
		instance.setEnabled(false);
	}

	public static HolographicDisplays getInstance() {
		return instance;
	}

	public HologramsCommandHandler getMainCommandHandler() {
		return mainCommandHandler;
	}

	public static PlaceholderManager getPlaceholderManager() {
		return placeholderManager;
	}
	
	public static void logInfo(String message) {
		logger.info(message);
	}
	
	public static void logWarning(String message) {
		logger.warning(message);
	}
	
	public static void logSevere(String message) {
		logger.severe(message);
	}
}
