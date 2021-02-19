/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays;

import me.filoghost.fcommons.BaseJavaPlugin;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.fcommons.reflection.ReflectUtils;
import me.filoghost.holographicdisplays.api.internal.BackendAPI;
import me.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import me.filoghost.holographicdisplays.bridge.protocollib.current.ProtocolLibHookImpl;
import me.filoghost.holographicdisplays.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.disk.upgrade.LegacySymbolsUpgrader;
import me.filoghost.holographicdisplays.listener.MainListener;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.DefaultBackendAPI;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.object.PluginHologram;
import me.filoghost.holographicdisplays.object.PluginHologramManager;
import me.filoghost.holographicdisplays.placeholder.AnimationsRegister;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import me.filoghost.holographicdisplays.task.BungeeCleanupTask;
import me.filoghost.holographicdisplays.task.StartupLoadHologramsTask;
import me.filoghost.holographicdisplays.task.WorldPlayerCounterTask;
import me.filoghost.holographicdisplays.util.NMSVersion;
import me.filoghost.holographicdisplays.util.VersionUtils;
import me.filoghost.updatechecker.UpdateChecker;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HolographicDisplays extends BaseJavaPlugin {
    
    // The main instance of the plugin.
    private static HolographicDisplays instance;
    
    // The manager for net.minecraft.server access.
    private static NMSManager nmsManager;
    
    // The listener for all the Bukkit and NMS events.
    private static MainListener mainListener;
    
    // The command handler, just in case a plugin wants to register more commands.
    private static HologramCommandManager commandManager;
    
    // The new version found by the updater, null if there is no new version.
    private static String newVersion;

    // Not null if ProtocolLib is installed and successfully loaded.
    private static ProtocolLibHook protocolLibHook;
    
    private static Path dataFolderPath;

    @Override
    public void onCheckedEnable() throws PluginEnableException {
        // Warn about plugin reloaders and the /reload command.
        if (instance != null || System.getProperty("HolographicDisplaysLoaded") != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[HolographicDisplays] Please do not use /reload or plugin reloaders. Use the command \"/holograms reload\" instead. You will receive no support for doing this operation.");
        }
        
        System.setProperty("HolographicDisplaysLoaded", "true");
        instance = this;
        dataFolderPath = getDataFolder().toPath();
        ConfigManager configManager = new ConfigManager(dataFolderPath);

        // Run only once at startup, before anything else.
        try {
            LegacySymbolsUpgrader.run(configManager);
        } catch (ConfigException e) {
            Log.warning("Couldn't convert symbols file", e);
        }

        // Load placeholders.yml.
        configManager.reloadCustomPlaceholders();

        // Load the configuration.
        configManager.reloadMainConfig();
        
        if (Configuration.updateNotification) {
            UpdateChecker.run(this, 75097, (String newVersion) -> {
                HolographicDisplays.newVersion = newVersion;
                Log.info("Found a new version available: " + newVersion);
                Log.info("Download it on Bukkit Dev:");
                Log.info("dev.bukkit.org/projects/holographic-displays");
            });
        }
        
        // The bungee chat API is required.
        if (!ReflectUtils.isClassLoaded("net.md_5.bungee.api.chat.ComponentBuilder")) {
            throw new PluginEnableException(
                    "Holographic Displays requires the new chat API.",
                    "You are probably running CraftBukkit instead of Spigot.");
        }
        
        if (!NMSVersion.isValid()) {
            throw new PluginEnableException(
                "Holographic Displays does not support this server version.",
                "Supported Spigot versions: from 1.8.3 to 1.16.4.");
        }
        
        try {
            nmsManager = NMSVersion.createNMSManager();
            nmsManager.setup();
        } catch (Exception e) {
            throw new PluginEnableException(e, "Couldn't initialize the NMS manager.");
        }
        
        // ProtocolLib check.
        hookProtocolLib();
        
        // Load animation files and the placeholder manager.
        PlaceholdersManager.load(this);
        try {
            AnimationsRegister.loadAnimations(this);
        } catch (Exception e) {
            Log.warning("Failed to load animation files!", e);
        }
        
        // Initialize other static classes.
        configManager.reloadHologramDatabase();
        BungeeServerTracker.restartTask(Configuration.bungeeRefreshSeconds);
        
        // Start repeating tasks.
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BungeeCleanupTask(), 5 * 60 * 20, 5 * 60 * 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorldPlayerCounterTask(), 0L, 3 * 20);
        
        if (getCommand("holograms") == null) {
            throw new PluginEnableException(
                "Holographic Displays was unable to register the command \"holograms\".",
                "This can be caused by edits to plugin.yml or other plugins.");
        }
        
        commandManager = new HologramCommandManager(configManager);
        commandManager.register(this);

        mainListener = new MainListener(nmsManager);
        Bukkit.getPluginManager().registerEvents(mainListener, this);

        // Register bStats metrics
        int pluginID = 3123;
        new MetricsLite(this, pluginID);
        
        // Holograms are loaded later, when the worlds are ready.
        Bukkit.getScheduler().runTask(this, 
                new StartupLoadHologramsTask(configManager.getHologramDatabase().getHolograms()));
        
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

    public static HologramCommandManager getCommandManager() {
        return commandManager;
    }

    public static HolographicDisplays getInstance() {
        return instance;
    }


    public static String getNewVersion() {
        return newVersion;
    }
    
    
    public void hookProtocolLib() {
        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            return;
        }

        try {
            String protocolVersion = Bukkit.getPluginManager().getPlugin("ProtocolLib").getDescription().getVersion();
            Matcher versionNumbersMatcher = Pattern.compile("([0-9\\.])+").matcher(protocolVersion);
            
            if (!versionNumbersMatcher.find()) {
                throw new IllegalArgumentException("could not find version numbers pattern");
            }
            
            String versionNumbers = versionNumbersMatcher.group();
            
            if (!VersionUtils.isVersionGreaterEqual(versionNumbers, "4.1")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Holographic Displays] Detected old version of ProtocolLib, support disabled. You must use ProtocolLib 4.1 or higher.");
                return;
            }
            
        } catch (Exception e) {
            Log.warning("Could not detect ProtocolLib version (" + e.getClass().getName() + ": " + e.getMessage() + "), enabling support anyway and hoping for the best. If you get errors, please contact the author.");
        }
        
        try {
            ProtocolLibHook protocolLibHook = new ProtocolLibHookImpl();
            
            if (protocolLibHook.hook(this, nmsManager)) {
                HolographicDisplays.protocolLibHook = protocolLibHook;
                Log.info("Enabled player relative placeholders with ProtocolLib.");
            }
        } catch (Exception e) {
            Log.warning("Failed to load ProtocolLib support. Is it updated?", e);
        }
    }
    
    
    public static boolean hasProtocolLibHook() {
        return protocolLibHook != null;
    }
    
    
    public static ProtocolLibHook getProtocolLibHook() {
        return protocolLibHook;
    }

    public static Path getDataFolderPath() {
        return dataFolderPath;
    }
    
}
