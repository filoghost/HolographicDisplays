/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin;

import com.gmail.filoghost.holographicdisplays.api.internal.HologramsAPIProvider;
import me.filoghost.fcommons.FCommonsPlugin;
import me.filoghost.fcommons.FeatureSupport;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.plugin.api.current.DefaultHolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramsAPIProvider;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.plugin.bridge.placeholderapi.PlaceholderAPIHook;
import me.filoghost.holographicdisplays.plugin.bridge.protocollib.ProtocolLibHook;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.disk.ConfigManager;
import me.filoghost.holographicdisplays.plugin.disk.Configuration;
import me.filoghost.holographicdisplays.plugin.disk.HologramDatabase;
import me.filoghost.holographicdisplays.plugin.disk.upgrade.LegacySymbolsUpgrade;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologram;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import me.filoghost.holographicdisplays.plugin.listener.ChunkListener;
import me.filoghost.holographicdisplays.plugin.listener.InteractListener;
import me.filoghost.holographicdisplays.plugin.listener.SpawnListener;
import me.filoghost.holographicdisplays.plugin.listener.UpdateNotificationListener;
import me.filoghost.holographicdisplays.plugin.log.PrintableErrorCollector;
import me.filoghost.holographicdisplays.plugin.placeholder.TickClock;
import me.filoghost.holographicdisplays.plugin.placeholder.TickingTask;
import me.filoghost.holographicdisplays.plugin.placeholder.internal.AnimationRegistry;
import me.filoghost.holographicdisplays.plugin.placeholder.internal.DefaultPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderLineTracker;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import me.filoghost.holographicdisplays.plugin.util.NMSVersion;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HolographicDisplays extends FCommonsPlugin implements ProtocolPacketSettings {

    private static HolographicDisplays instance;

    private ConfigManager configManager;
    private InternalHologramManager internalHologramManager;
    private APIHologramManager apiHologramManager;
    private BungeeServerTracker bungeeServerTracker;
    private AnimationRegistry animationRegistry;
    private PlaceholderRegistry placeholderRegistry;

    @Override
    public void onCheckedEnable() throws PluginEnableException {
        // Warn about plugin reloaders and the /reload command
        if (instance != null || System.getProperty("HolographicDisplaysLoaded") != null) {
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.RED + "[HolographicDisplays] Please do not use /reload or plugin reloaders."
                            + " Use the command \"/holograms reload\" instead."
                            + " You will receive no support for doing this operation.");
        }

        System.setProperty("HolographicDisplaysLoaded", "true");
        instance = this;

        // The bungee chat API is required
        if (!FeatureSupport.CHAT_COMPONENTS) {
            throw new PluginEnableException(
                    "Holographic Displays requires the new chat API.",
                    "You are probably running CraftBukkit instead of Spigot.");
        }

        if (!NMSVersion.isValid()) {
            throw new PluginEnableException(
                    "Holographic Displays does not support this server version.",
                    "Supported Spigot versions: from 1.8.3 to 1.17.");
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

        configManager = new ConfigManager(getDataFolder().toPath());
        bungeeServerTracker = new BungeeServerTracker(this);
        animationRegistry = new AnimationRegistry();
        placeholderRegistry = new PlaceholderRegistry();
        TickClock tickClock = new TickClock();
        PlaceholderTracker placeholderTracker = new PlaceholderTracker(placeholderRegistry, tickClock);
        PlaceholderLineTracker placeholderLineTracker = new PlaceholderLineTracker(placeholderTracker);
        internalHologramManager = new InternalHologramManager(nmsManager, placeholderLineTracker);
        apiHologramManager = new APIHologramManager(nmsManager, placeholderLineTracker);

        PrintableErrorCollector errorCollector = new PrintableErrorCollector();

        // Run only once at startup, before anything else
        try {
            LegacySymbolsUpgrade.run(configManager, errorCollector);
        } catch (ConfigException e) {
            errorCollector.add(e, "couldn't automatically convert symbols file to the new format");
        }

        load(true, errorCollector);

        ProtocolLibHook.setup(this, nmsManager, this, placeholderLineTracker, errorCollector);
        PlaceholderAPIHook.setup();

        TickingTask tickingTask = new TickingTask(tickClock, placeholderLineTracker);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, tickingTask, 0, 1);

        HologramCommandManager commandManager = new HologramCommandManager(configManager, internalHologramManager, nmsManager);
        commandManager.register(this);

        registerListener(new InteractListener(nmsManager));
        registerListener(new SpawnListener(nmsManager));
        registerListener(new ChunkListener(nmsManager, internalHologramManager, apiHologramManager));
        UpdateNotificationListener updateNotificationListener = new UpdateNotificationListener();
        registerListener(updateNotificationListener);

        // Enable the APIs
        HolographicDisplaysAPIProvider.setImplementation(
                new DefaultHolographicDisplaysAPIProvider(apiHologramManager, nmsManager, placeholderRegistry));
        enableLegacyAPI(apiHologramManager, nmsManager, placeholderRegistry);

        // Register bStats metrics
        int pluginID = 3123;
        new MetricsLite(this, pluginID);

        updateNotificationListener.runAsyncUpdateCheck();

        if (errorCollector.hasErrors()) {
            errorCollector.logToConsole();
            Bukkit.getScheduler().runTaskLater(this, errorCollector::logErrorCount, 10L);
        }
    }

    @SuppressWarnings("deprecation")
    private void enableLegacyAPI(APIHologramManager apiHologramManager, NMSManager nmsManager, PlaceholderRegistry placeholderRegistry) {
        HologramsAPIProvider.setImplementation(new V2HologramsAPIProvider(
                apiHologramManager,
                nmsManager,
                placeholderRegistry));
    }

    public void load(boolean deferHologramsCreation, ErrorCollector errorCollector) {
        DefaultPlaceholders.resetAndRegister(placeholderRegistry, animationRegistry, bungeeServerTracker);

        internalHologramManager.clearAll();

        configManager.reloadStaticReplacements(errorCollector);
        configManager.reloadMainConfig(errorCollector);
        HologramDatabase hologramDatabase = configManager.loadHologramDatabase(errorCollector);
        try {
            animationRegistry.loadAnimations(configManager, errorCollector);
        } catch (IOException | ConfigException e) {
            errorCollector.add(e, "failed to load animation files");
        }

        bungeeServerTracker.restart(Configuration.bungeeRefreshSeconds, TimeUnit.SECONDS);

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
