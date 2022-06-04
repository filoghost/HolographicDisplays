/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin;

import me.filoghost.fcommons.FCommonsPlugin;
import me.filoghost.fcommons.FeatureSupport;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.core.HolographicDisplaysCore;
import me.filoghost.holographicdisplays.plugin.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.plugin.bridge.placeholderapi.PlaceholderAPIHook;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.config.ConfigManager;
import me.filoghost.holographicdisplays.plugin.config.InternalHologramConfig;
import me.filoghost.holographicdisplays.plugin.config.InternalHologramLoadException;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import me.filoghost.holographicdisplays.plugin.config.upgrade.AnimationsLegacyUpgrade;
import me.filoghost.holographicdisplays.plugin.config.upgrade.DatabaseLegacyUpgrade;
import me.filoghost.holographicdisplays.plugin.config.upgrade.SymbolsLegacyUpgrade;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;
import me.filoghost.holographicdisplays.plugin.internal.placeholder.AnimationPlaceholderFactory;
import me.filoghost.holographicdisplays.plugin.internal.placeholder.DefaultPlaceholders;
import me.filoghost.holographicdisplays.plugin.listener.UpdateNotificationListener;
import me.filoghost.holographicdisplays.plugin.log.PrintableErrorCollector;
import me.filoghost.updatechecker.UpdateChecker;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HolographicDisplays extends FCommonsPlugin {

    private static HolographicDisplays instance;

    private HolographicDisplaysCore core;
    private HolographicDisplaysAPI api;
    private ConfigManager configManager;
    private BungeeServerTracker bungeeServerTracker;
    private InternalHologramManager internalHologramManager;
    private InternalHologramEditor internalHologramEditor;

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

        if (!FeatureSupport.CHAT_COMPONENTS) {
            throw new PluginEnableException(
                    "Holographic Displays requires the new chat API.",
                    "You are probably running CraftBukkit instead of Spigot.");
        }

        if (getCommand("holograms") == null) {
            throw new PluginEnableException(
                    "Holographic Displays was unable to register the command \"holograms\".",
                    "This can be caused by edits to plugin.yml or other plugins.");
        }

        PrintableErrorCollector errorCollector = new PrintableErrorCollector();

        core = new HolographicDisplaysCore();
        core.enable(this, errorCollector);
        api = HolographicDisplaysAPI.get(this);

        configManager = new ConfigManager(getDataFolder().toPath());
        bungeeServerTracker = new BungeeServerTracker(this);
        internalHologramManager = new InternalHologramManager(api);

        // Run only once at startup, before loading the configuration
        new SymbolsLegacyUpgrade(configManager, errorCollector).tryRun();
        new AnimationsLegacyUpgrade(configManager, errorCollector).tryRun();
        new DatabaseLegacyUpgrade(configManager, errorCollector).tryRun();

        // Load the configuration
        load(errorCollector);

        // Commands
        internalHologramEditor = new InternalHologramEditor(internalHologramManager, configManager);
        new HologramCommandManager(this, internalHologramEditor).register(this);

        // Setup external plugin hooks
        PlaceholderAPIHook.setup();

        // Register bStats metrics
        int bStatsPluginID = 3123;
        new MetricsLite(this, bStatsPluginID);

        // Log all loading errors at the end
        if (errorCollector.hasErrors()) {
            errorCollector.logToConsole();
            Bukkit.getScheduler().runTaskLater(this, errorCollector::logSummaryToConsole, 10L);
        }

        // Run the update checker
        if (Settings.updateNotification) {
            int bukkitPluginID = 75097;
            UpdateChecker.run(this, bukkitPluginID, (String newVersion) -> {
                registerListener(new UpdateNotificationListener(newVersion));
                Log.info("Found a new version available: " + newVersion);
                Log.info("Download it on Bukkit Dev:");
                Log.info("https://dev.bukkit.org/projects/holographic-displays");
            });
        }
    }

    public void load(ErrorCollector errorCollector) {
        internalHologramManager.deleteHolograms();

        configManager.reloadStaticReplacements(errorCollector);
        configManager.reloadMainSettings(errorCollector);

        AnimationPlaceholderFactory animationPlaceholderFactory = configManager.loadAnimations(errorCollector);
        DefaultPlaceholders.resetAndRegister(api, animationPlaceholderFactory, bungeeServerTracker);

        bungeeServerTracker.restart(Settings.bungeeRefreshSeconds, TimeUnit.SECONDS);

        // Load holograms from database
        List<InternalHologramConfig> hologramConfigs = configManager.readHologramDatabase(errorCollector);
        for (InternalHologramConfig hologramConfig : hologramConfigs) {
            try {
                List<InternalHologramLine> lines = hologramConfig.deserializeLines();
                Position position = hologramConfig.deserializePosition();
                InternalHologram hologram = internalHologramManager.createHologram(hologramConfig.getName(), position);
                hologram.addLines(lines);
            } catch (InternalHologramLoadException e) {
                errorCollector.add(e, "error while loading hologram \"" + hologramConfig.getName() + "\"");
            }
        }

        core.setSpaceBetweenHologramLines(Settings.spaceBetweenLines);
    }

    @Override
    public void onDisable() {
        core.disable();
    }

    public static HolographicDisplays getInstance() {
        return instance;
    }

    public InternalHologramEditor getInternalHologramEditor() {
        return internalHologramEditor;
    }

    public InternalHologramManager getInternalHologramManager() {
        return internalHologramManager;
    }

}
