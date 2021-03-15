/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays;

import me.filoghost.fcommons.FCommonsPlugin;
import me.filoghost.fcommons.FeatureSupport;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.api.internal.BackendAPI;
import me.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import me.filoghost.holographicdisplays.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.disk.upgrade.LegacySymbolsUpgrader;
import me.filoghost.holographicdisplays.listener.ChunkListener;
import me.filoghost.holographicdisplays.listener.InteractListener;
import me.filoghost.holographicdisplays.listener.SpawnListener;
import me.filoghost.holographicdisplays.listener.UpdateNotificationListener;
import me.filoghost.holographicdisplays.log.PrintableErrorCollector;
import me.filoghost.holographicdisplays.object.api.APIHologram;
import me.filoghost.holographicdisplays.object.api.APIHologramManager;
import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import me.filoghost.holographicdisplays.object.internal.InternalHologramManager;
import me.filoghost.holographicdisplays.placeholder.AnimationsRegistry;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import me.filoghost.holographicdisplays.task.BungeeCleanupTask;
import me.filoghost.holographicdisplays.task.WorldPlayerCounterTask;
import me.filoghost.holographicdisplays.util.NMSVersion;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.IOException;

public class HolographicDisplays extends FCommonsPlugin implements ProtocolPacketSettings {
    
    private static HolographicDisplays instance;

    private ConfigManager configManager;
    private InternalHologramManager internalHologramManager;
    private APIHologramManager apiHologramManager;
    private BungeeServerTracker bungeeServerTracker;
    private AnimationsRegistry animationRegistry;
    private PlaceholdersManager placeholderManager;

    @Override
    public void onCheckedEnable() throws PluginEnableException {
        // Warn about plugin reloaders and the /reload command.
        if (instance != null || System.getProperty("HolographicDisplaysLoaded") != null) {
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "[HolographicDisplays] Please do not use /reload or plugin reloaders." 
                            + " Use the command \"/holograms reload\" instead." 
                            + " You will receive no support for doing this operation.");
        }
        
        System.setProperty("HolographicDisplaysLoaded", "true");
        instance = this;

        // The bungee chat API is required.
        if (!FeatureSupport.CHAT_COMPONENTS) {
            throw new PluginEnableException(
                    "Holographic Displays requires the new chat API.",
                    "You are probably running CraftBukkit instead of Spigot.");
        }

        if (!NMSVersion.isValid()) {
            throw new PluginEnableException(
                    "Holographic Displays does not support this server version.",
                    "Supported Spigot versions: from 1.8.3 to 1.16.4.");
        }

        if (getCommand("holograms") == null) {
            throw new PluginEnableException(
                    "Holographic Displays was unable to register the command \"holograms\".",
                    "This can be caused by edits to plugin.yml or other plugins.");
        }
        
        NMSManager nmsManager;
        try {
            nmsManager = NMSVersion.createNMSManager(this);
            nmsManager.setup();
        } catch (Exception e) {
            throw new PluginEnableException(e, "Couldn't initialize the NMS manager.");
        }
        
        bungeeServerTracker = new BungeeServerTracker(this);
        animationRegistry = new AnimationsRegistry();
        placeholderManager = new PlaceholdersManager(bungeeServerTracker, animationRegistry);
        configManager = new ConfigManager(getDataFolder().toPath());
        internalHologramManager = new InternalHologramManager(nmsManager, placeholderManager);
        apiHologramManager = new APIHologramManager(nmsManager, placeholderManager);

        PrintableErrorCollector errorCollector = new PrintableErrorCollector();

        // Run only once at startup, before anything else.
        try {
            LegacySymbolsUpgrader.run(configManager, errorCollector);
        } catch (ConfigException e) {
            errorCollector.add(e, "couldn't convert symbols file");
        }
        
        load(true, errorCollector);
        
        ProtocolLibHook.setup(this, nmsManager, this, errorCollector);
        
        // Start repeating tasks.
        placeholderManager.startRefreshTask(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BungeeCleanupTask(bungeeServerTracker), 5 * 60 * 20, 5 * 60 * 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new WorldPlayerCounterTask(), 0L, 3 * 20);

        HologramCommandManager commandManager = new HologramCommandManager(configManager, internalHologramManager, nmsManager);
        commandManager.register(this);
        
        registerListener(new InteractListener(nmsManager));
        registerListener(new SpawnListener(nmsManager));
        registerListener(new ChunkListener(nmsManager, internalHologramManager, apiHologramManager));
        UpdateNotificationListener updateNotificationListener = new UpdateNotificationListener();
        registerListener(updateNotificationListener);
        
        // Enable the API.
        BackendAPI.setImplementation(new DefaultBackendAPI(apiHologramManager, nmsManager, placeholderManager.getRegistry()));

        // Register bStats metrics
        int pluginID = 3123;
        new MetricsLite(this, pluginID);
        
        updateNotificationListener.runAsyncUpdateCheck();

        if (errorCollector.hasErrors()) {
            errorCollector.logToConsole();
            Bukkit.getScheduler().runTaskLater(this, errorCollector::logErrorCount, 10L);
        }
    }
    
    public void load(boolean deferHologramsCreation, ErrorCollector errorCollector) {
        placeholderManager.untrackAll();
        internalHologramManager.clearAll();
        bungeeServerTracker.resetTrackedServers();
        
        configManager.reloadCustomPlaceholders(errorCollector);
        configManager.reloadMainConfig(errorCollector);
        HologramDatabase hologramDatabase = configManager.loadHologramDatabase(errorCollector);
        try {
            animationRegistry.loadAnimations(configManager, errorCollector);
        } catch (IOException | ConfigException e) {
            errorCollector.add(e, "failed to load animation files");
        }
        
        bungeeServerTracker.restartTask(Configuration.bungeeRefreshSeconds);
        
        if (deferHologramsCreation) {
            // For the initial load: holograms are loaded later, when the worlds are ready
            Bukkit.getScheduler().runTask(this, () -> hologramDatabase.createHolograms(internalHologramManager, errorCollector));
        } else {
            hologramDatabase.createHolograms(internalHologramManager, errorCollector);
        }
    }

    @Override
    public void onDisable() {
        if (internalHologramManager != null) {
            for (InternalHologram hologram : internalHologramManager.getHolograms()) {
                hologram.despawnEntities();
            }
        }
        if (apiHologramManager != null) {
            for (APIHologram hologram : apiHologramManager.getHolograms()) {
                hologram.despawnEntities();
            }
        }
    }

    public static HolographicDisplays getInstance() {
        return instance;
    }
    
    @Override
    public boolean sendAccurateLocationPackets() {
        return ProtocolLibHook.isEnabled();
    }

}
