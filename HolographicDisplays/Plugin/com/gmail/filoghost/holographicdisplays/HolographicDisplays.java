package com.gmail.filoghost.holographicdisplays;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.SimpleUpdater.ResponseHandler;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import com.gmail.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramsCommandHandler;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.disk.UnicodeSymbols;
import com.gmail.filoghost.holographicdisplays.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicdisplays.exception.InvalidFormatException;
import com.gmail.filoghost.holographicdisplays.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicdisplays.listener.MainListener;
import com.gmail.filoghost.holographicdisplays.metrics.MetricsLite;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.placeholder.AnimationsRegister;
import com.gmail.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import com.gmail.filoghost.holographicdisplays.task.BungeeCleanupTask;
import com.gmail.filoghost.holographicdisplays.task.WorldPlayerCounterTask;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;

public class HolographicDisplays extends JavaPlugin {

	// The main instance of the plugin.
	private static HolographicDisplays instance;
	
	// The manager for net.minecraft.server access.
	private static NMSManager nmsManager;
	
	// Since 1.8 we use armor stands instead of wither skulls.
	private static boolean is1_8;
	
	// True if ProtocolLib is installed and successfully loaded.
	private static boolean useProtocolLib;
	
	// The new version found by the updater, null if there is no new version.
	private static String newVersion;
	
	@Override
	public void onEnable() {
		
		// Blocks plugin reloaders and the /reload command.
		if (instance != null) {
			getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/holograms reload\" instead.");
			return;
		}
		
		instance = this;
		
		// Load placeholders.yml.
		UnicodeSymbols.load(this);

		// Load the configuration.
		Configuration.load(this);
		
		if (Configuration.updateNotification) {
			new SimpleUpdater(this, 75097).checkForUpdates(new ResponseHandler() {
				
				@Override
				public void onUpdateFound(final String newVersion) {

					HolographicDisplays.newVersion = newVersion;
					getLogger().info("Found a new version available: " + newVersion);
					getLogger().info("Download it on Bukkit Dev:");
					getLogger().info("dev.bukkit.org/bukkit-plugins/holographic-displays");					
				}
			});
		}
		
		String version = VersionUtils.getBukkitVersion();
		
		if (version == null) {
			// Caused by MCPC+ / Cauldron renaming packages, extract the version from Bukkit.getVersion().
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
			} else if ("1.8".equals(version)) {
				version = "v1_8_R1";
			} else {
				// Cannot definitely get the version. This will cause the plugin to disable itself.
				version = null;
			}
		}
		
		// It's simple, we don't need reflection.
		if ("v1_6_R3".equals(version)) {
			nmsManager = new com.gmail.filoghost.holographicdisplays.nms.v1_6_R3.NmsManagerImpl();
		} else if ("v1_7_R1".equals(version)) {
			nmsManager = new com.gmail.filoghost.holographicdisplays.nms.v1_7_R1.NmsManagerImpl();
		} else if ("v1_7_R2".equals(version)) {
			nmsManager = new com.gmail.filoghost.holographicdisplays.nms.v1_7_R2.NmsManagerImpl();
		} else if ("v1_7_R3".equals(version)) {
			nmsManager = new com.gmail.filoghost.holographicdisplays.nms.v1_7_R3.NmsManagerImpl();
		} else if ("v1_7_R4".equals(version)) {
			nmsManager = new com.gmail.filoghost.holographicdisplays.nms.v1_7_R4.NmsManagerImpl();
		} else if ("v1_8_R1".equals(version)) {
			is1_8 = true;
			nmsManager = new com.gmail.filoghost.holographicdisplays.nms.v1_8_R1.NmsManagerImpl();
		} else {
			printWarnAndDisable(
				"******************************************************",
				"     This version of HolographicDisplays can",
				"     only work on these server versions:",
				"     1.6.4, from 1.7 to 1.8.1.",
				"     The plugin will be disabled.",
				"******************************************************"
			);
			return;
		}

		try {
			if (VersionUtils.isMCPCOrCauldron()) {
				getLogger().info("Trying to enable Cauldron/MCPC+ support...");
			}
			
			nmsManager.setup();
			
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
				useProtocolLib = ProtocolLibHook.load(nmsManager, this, is1_8);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().warning("Failed to load ProtocolLib support. Is it updated?");
		}
		
		// Load animation files and the placeholder manager.
		PlaceholdersManager.load(this);
		try {
			AnimationsRegister.loadAnimations(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().warning("Failed to load animation files!");
		}
		
		// Initalize other static classes.
		HologramDatabase.loadYamlFile(this);
		BungeeServerTracker.startTask(Configuration.bungeeRefreshSeconds);
		
		// Start repeating tasks.
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BungeeCleanupTask(), 5 * 60 * 20, 5 * 60 * 20);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorldPlayerCounterTask(), 0L, 3 * 20);
		
		Set<String> savedHologramsNames = HologramDatabase.getHolograms();
		if (savedHologramsNames != null && savedHologramsNames.size() > 0) {
			for (String singleHologramName : savedHologramsNames) {
				try {
					NamedHologram singleHologram = HologramDatabase.loadHologram(singleHologramName);
					NamedHologramManager.addHologram(singleHologram);
				} catch (HologramNotFoundException e) {
					getLogger().warning("Hologram '" + singleHologramName + "' not found, skipping it.");
				} catch (InvalidFormatException e) {
					getLogger().warning("Hologram '" + singleHologramName + "' has an invalid location format.");
				} catch (WorldNotFoundException e) {
					getLogger().warning("Hologram '" + singleHologramName + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
				} catch (Exception e) {
					e.printStackTrace();
					getLogger().warning("Unhandled exception while loading the hologram '" + singleHologramName + "'. Please contact the developer.");
				}
			}
		}
		
		getCommand("holograms").setExecutor(new HologramsCommandHandler());
		Bukkit.getPluginManager().registerEvents(new MainListener(nmsManager), this);
		
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (Exception ignore) { }
		
		// The entities are loaded when the server is ready.
		new BukkitRunnable() {
			@Override
			public void run() {
				for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
					hologram.refreshAll();
				}
			}
		}.runTaskLater(this, 10L);
	}
	

	@Override
	public void onDisable() {
		for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
			hologram.despawnEntities();
		}
	}
	
	public static NMSManager getNMSManager() {
		return nmsManager;
	}
	
	public static boolean is1_8() {
		return is1_8;
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


	public static String getNewVersion() {
		return newVersion;
	}
	
	
	public static boolean useProtocolLib() {
		return useProtocolLib;
	}
	
}
