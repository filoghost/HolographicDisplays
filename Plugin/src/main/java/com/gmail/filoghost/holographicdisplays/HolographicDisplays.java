package com.gmail.filoghost.holographicdisplays;

import com.gmail.filoghost.holographicdisplays.api.internal.BackendAPI;
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
import com.gmail.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import com.gmail.filoghost.holographicdisplays.object.*;
import com.gmail.filoghost.holographicdisplays.placeholder.AnimationsRegister;
import com.gmail.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import com.gmail.filoghost.holographicdisplays.task.BungeeCleanupTask;
import com.gmail.filoghost.holographicdisplays.task.StartupLoadHologramsTask;
import com.gmail.filoghost.holographicdisplays.task.WorldPlayerCounterTask;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.BukkitVersion;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HolographicDisplays extends JavaPlugin {

	// The main instance of the plugin.
	private static HolographicDisplays instance;

	// The manager for net.minecraft.server access.
	private static NMSManager nmsManager;

	// The listener for all the Bukkit and NMS events.
	private static MainListener mainListener;

	// The command handler, just in case a plugin wants to register more commands.
	private HologramsCommandHandler commandHandler;

	// The new version found by the updater, null if there is no new version.
	private static String newVersion;

	// Not null if ProtocolLib is installed and successfully loaded.
	private static ProtocolLibHook protocolLibHook;

	@Override
	public void onLoad() {
		// Prepare the logger.
		ConsoleLogger.setLogger(getLogger());
	}

	@Override
	public void onEnable() {
		// Check instance, warn if the plugin was already loaded (before a /reload).
		if (instance != null || System.getProperty("HolographicDisplaysLoaded") != null) {
			ConsoleLogger.warning("Please do not use /reload or plugin reloaders. Use the command \"/holograms reload\" instead. You will receive no support for doing this operation.");
		}
		System.setProperty("HolographicDisplaysLoaded", "true");
		instance = this;

		// Load placeholders.yml.
		UnicodeSymbols.load(this);

		// Load the configuration.
		Configuration.load(this);

		// Update notifications.
		if (Configuration.updateNotification) {
			new SimpleUpdater(this, 75097).checkForUpdates(newVersion -> {
				HolographicDisplays.newVersion = newVersion;
				ConsoleLogger.info("Found a new version available: " + newVersion);
				ConsoleLogger.info("Download it on Bukkit Dev:");
				ConsoleLogger.info("dev.bukkit.org/bukkit-plugins/holographic-displays");
			});
		}

		// Check minimum server version.
		if (!BukkitVersion.isAtLeast(BukkitVersion.v1_8_R1)) {
			printWarnAndDisable(
					"******************************************************",
					"     This version of HolographicDisplays only",
					"     works on server versions from 1.8 to 1.13.",
					"     The plugin will be disabled.",
					"******************************************************"
			);
			return;
		}

		// Construct the proper NMS adapter.
		try {
			nmsManager = (NMSManager) Class.forName("com.gmail.filoghost.holographicdisplays.nms." + BukkitVersion.getCurrentVersion() + ".NmsManagerImpl").getConstructor().newInstance();
		} catch (Throwable t) {
			printWarnAndDisable(t,
					"******************************************************",
					"     HolographicDisplays was unable to instantiate",
					"     the NMS manager. The plugin will be disabled.",
					"******************************************************"
			);
			return;
		}

		// Setup the NMS adapter.
		try {
			if (BukkitUtils.isForgeServer()) {
				ConsoleLogger.info("Trying to enable Forge support...");
			}
			nmsManager.setup();
			if (BukkitUtils.isForgeServer()) {
				ConsoleLogger.info("Successfully added Forge support!");
			}
		} catch (Exception e) {
			printWarnAndDisable(e,
					"******************************************************",
					"     HolographicDisplays was unable to register",
					"     custom entities, the plugin will be disabled.",
					"     Are you using the correct Bukkit/Spigot version?",
					"******************************************************"
			);
			return;
		}

		// Check ProtocolLib.
		try {
			if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
				String requiredVersionError = null;

				try {
					String protocolVersion = getServer().getPluginManager().getPlugin("ProtocolLib").getDescription().getVersion();
					Matcher versionNumbersMatcher = Pattern.compile("([0-9.])+").matcher(protocolVersion);
					if (versionNumbersMatcher.find()) {
						String versionNumbers = versionNumbersMatcher.group();
						if (!VersionUtils.isVersionGreaterEqual(versionNumbers, "4.4.0")) {
							requiredVersionError = "higher than or equal to 4.4.0-SNAPSHOT";
						}
					} else {
						throw new RuntimeException("could not find version numbers pattern");
					}
				} catch (Exception e) {
					ConsoleLogger.warning("Could not check ProtocolLib version (" + e.getClass().getName() + ": " + e.getMessage() + "), enabling support anyway and hoping for the best. If you get errors, please contact the author.");
				}

				if (requiredVersionError == null) {
					ConsoleLogger.info("Found ProtocolLib, hooking in it.");
					ProtocolLibHook protocolLibHook = new ProtocolLibHook();
					if (protocolLibHook.hook(this, nmsManager)) {
						HolographicDisplays.protocolLibHook = protocolLibHook;
						ConsoleLogger.info("Enabled player relative placeholders with ProtocolLib.");
					}
				} else {
					ConsoleLogger.warning("Detected incompatible version of ProtocolLib, support disabled. " +
							"For this server version you must be using a ProtocolLib version " + requiredVersionError + ".");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ConsoleLogger.warning("Failed to load ProtocolLib support. Is it updated?");
		}

		// Load animation files and the placeholder manager.
		PlaceholdersManager.load(this);
		try {
			AnimationsRegister.loadAnimations(this);
		} catch (Exception e) {
			ConsoleLogger.error("Failed to load animation files!", e);
		}

		// Initialize other static classes.
		HologramDatabase.loadYamlFile(this);
		BungeeServerTracker.startTask(Configuration.bungeeRefreshSeconds);

		// Start repeating tasks.
		new BungeeCleanupTask().runTaskTimer(this, 5 * 60 * 20, 5 * 6);
		new WorldPlayerCounterTask().runTaskTimer(this, 0L, 3 * 20);

		// Load holograms.
		Set<String> savedHologramsNames = HologramDatabase.getHolograms();
		if (savedHologramsNames != null && savedHologramsNames.size() > 0) {
			for (String singleHologramName : savedHologramsNames) {
				try {
					NamedHologram singleHologram = HologramDatabase.loadHologram(singleHologramName);
					NamedHologramManager.addHologram(singleHologram);
				} catch (HologramNotFoundException e) {
					ConsoleLogger.warning("Hologram '" + singleHologramName + "' not found, skipping it.");
				} catch (InvalidFormatException e) {
					ConsoleLogger.warning("Hologram '" + singleHologramName + "' has an invalid location format.");
				} catch (WorldNotFoundException e) {
					ConsoleLogger.warning("Hologram '" + singleHologramName + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
				} catch (Exception e) {
					ConsoleLogger.error("Unhandled exception while loading the hologram '" + singleHologramName + "'. Please contact the developer.", e);
				}
			}
		}

		// Check if base command is available.
		if (getCommand("holograms") == null) {
			printWarnAndDisable(
					"******************************************************",
					"     HolographicDisplays was unable to register",
					"     the command \"holograms\". Do not modify",
					"     plugin.yml removing commands, if this is",
					"     the case.",
					"******************************************************"
			);
			return;
		}

		// Register commands.
		getCommand("holograms").setExecutor(commandHandler = new HologramsCommandHandler());

		// Register events
		getServer().getPluginManager().registerEvents(mainListener = new MainListener(nmsManager), this);

		// Send metrics data.
		try {
			new MetricsLite(this);
		} catch (Exception ignore) {
		}

		// The entities are loaded when the server is ready.
		new StartupLoadHologramsTask().runTask(this);

		// Enable the API.
		BackendAPI.setImplementation(new DefaultBackendAPI());
	}

	@Override
	public void onDisable() {
		for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
			hologram.despawnEntities();
		}
		for (PluginHologram hologram : PluginHologramManager.getHolograms()) {
			hologram.despawnEntities();
		}
	}

	public static NMSManager getNMSManager() {
		return nmsManager;
	}

	public static MainListener getMainListener() {
		return mainListener;
	}

	public HologramsCommandHandler getCommandHandler() {
		return commandHandler;
	}

	private static void printWarnAndDisable(Throwable t, String... messages) {
		ConsoleLogger.error(t);
		printWarnAndDisable(messages);
	}

	private static void printWarnAndDisable(String... messages) {
		for (String message : messages) {
			ConsoleLogger.warning(message);
		}
		instance.setEnabled(false);
	}

	public static HolographicDisplays getInstance() {
		return instance;
	}


	public static String getNewVersion() {
		return newVersion;
	}


	public static boolean hasProtocolLibHook() {
		return protocolLibHook != null;
	}


	public static ProtocolLibHook getProtocolLibHook() {
		return protocolLibHook;
	}

	public static boolean isConfigFile(File file) {
		return file.getName().toLowerCase().endsWith(".yml") && HolographicDisplays.getInstance().getResource(file.getName()) != null;
	}
}
