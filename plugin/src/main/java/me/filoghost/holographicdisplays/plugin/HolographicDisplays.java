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
import me.filoghost.holographicdisplays.plugin.api.current.DefaultHolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramsAPIProvider;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.plugin.bridge.placeholderapi.PlaceholderAPIHook;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.disk.ConfigManager;
import me.filoghost.holographicdisplays.plugin.disk.HologramDatabase;
import me.filoghost.holographicdisplays.plugin.disk.Settings;
import me.filoghost.holographicdisplays.plugin.disk.upgrade.LegacySymbolsUpgrade;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTouchListener;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.listener.ChunkListener;
import me.filoghost.holographicdisplays.plugin.listener.PlayerListener;
import me.filoghost.holographicdisplays.plugin.listener.UpdateNotificationListener;
import me.filoghost.holographicdisplays.plugin.log.PrintableErrorCollector;
import me.filoghost.holographicdisplays.plugin.placeholder.TickClock;
import me.filoghost.holographicdisplays.plugin.placeholder.TickingTask;
import me.filoghost.holographicdisplays.plugin.placeholder.internal.AnimationRegistry;
import me.filoghost.holographicdisplays.plugin.placeholder.internal.DefaultPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import me.filoghost.holographicdisplays.plugin.util.NMSVersion;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HolographicDisplays extends FCommonsPlugin {

    private static HolographicDisplays instance;

    private NMSManager nmsManager;
    private ConfigManager configManager;
    private InternalHologramManager internalHologramManager;
    private BungeeServerTracker bungeeServerTracker;
    private AnimationRegistry animationRegistry;
    private PlaceholderRegistry placeholderRegistry;
    private LineTrackerManager lineTrackerManager;

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

        PrintableErrorCollector errorCollector = new PrintableErrorCollector();

        try {
            nmsManager = NMSVersion.getCurrent().createNMSManager(errorCollector);
        } catch (Throwable t) {
            throw new PluginEnableException(t, "Couldn't initialize the NMS manager.");
        }

        configManager = new ConfigManager(getDataFolder().toPath());
        bungeeServerTracker = new BungeeServerTracker(this);
        animationRegistry = new AnimationRegistry();
        placeholderRegistry = new PlaceholderRegistry();
        TickClock tickClock = new TickClock();
        PlaceholderTracker placeholderTracker = new PlaceholderTracker(placeholderRegistry, tickClock);
        LineTouchListener lineTouchListener = new LineTouchListener();
        lineTrackerManager = new LineTrackerManager(nmsManager, placeholderTracker, lineTouchListener);
        internalHologramManager = new InternalHologramManager(lineTrackerManager);
        APIHologramManager apiHologramManager = new APIHologramManager(lineTrackerManager);

        // Run only once at startup, before anything else
        try {
            LegacySymbolsUpgrade.run(configManager, errorCollector);
        } catch (ConfigException e) {
            errorCollector.add(e, "couldn't automatically convert symbols file to the new format");
        }

        load(true, errorCollector);

        PlaceholderAPIHook.setup();

        for (Player player : Bukkit.getOnlinePlayers()) {
            nmsManager.injectPacketListener(player, lineTouchListener);
        }

        TickingTask tickingTask = new TickingTask(tickClock, lineTrackerManager, lineTouchListener);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, tickingTask, 0, 1);

        HologramCommandManager commandManager = new HologramCommandManager(this, internalHologramManager, configManager);
        commandManager.register(this);

        registerListener(new PlayerListener(nmsManager, lineTrackerManager, lineTouchListener));
        registerListener(new ChunkListener(this, lineTrackerManager));
        UpdateNotificationListener updateNotificationListener = new UpdateNotificationListener();
        registerListener(updateNotificationListener);

        // Enable the APIs
        HolographicDisplaysAPIProvider.setImplementation(
                new DefaultHolographicDisplaysAPIProvider(apiHologramManager, nmsManager, placeholderRegistry));
        enableLegacyAPI(apiHologramManager, nmsManager, placeholderRegistry);

        // Register bStats metrics
        int pluginID = 3123;
        new MetricsLite(this, pluginID);

        updateNotificationListener.runAsyncUpdateCheck(this);

        if (errorCollector.hasErrors()) {
            errorCollector.logToConsole();
            Bukkit.getScheduler().runTaskLater(this, errorCollector::logSummaryToConsole, 10L);
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
        DefaultPlaceholders.resetAndRegister(this, placeholderRegistry, animationRegistry, bungeeServerTracker);

        internalHologramManager.clearAll();

        configManager.reloadStaticReplacements(errorCollector);
        configManager.reloadMainSettings(errorCollector);
        HologramDatabase hologramDatabase = configManager.loadHologramDatabase(errorCollector);
        try {
            animationRegistry.loadAnimations(configManager, errorCollector);
        } catch (IOException | ConfigException e) {
            errorCollector.add(e, "failed to load animation files");
        }

        bungeeServerTracker.restart(Settings.bungeeRefreshSeconds, TimeUnit.SECONDS);

        if (deferHologramsCreation) {
            // For the initial load: holograms are loaded later, when the worlds are ready
            Bukkit.getScheduler().runTask(this, () -> hologramDatabase.createHolograms(internalHologramManager, errorCollector));
        } else {
            hologramDatabase.createHolograms(internalHologramManager, errorCollector);
        }
    }

    @Override
    public void onDisable() {
        lineTrackerManager.clearTrackedPlayersAndSendPackets();
        for (Player player : Bukkit.getOnlinePlayers()) {
            nmsManager.uninjectPacketListener(player);
        }
    }

    public static HolographicDisplays getInstance() {
        return instance;
    }

}
