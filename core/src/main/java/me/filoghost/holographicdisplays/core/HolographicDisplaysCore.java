/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core;

import com.gmail.filoghost.holographicdisplays.api.internal.HologramsAPIProvider;
import me.filoghost.fcommons.FCommonsPlugin.PluginEnableException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.core.NMSVersion.OutdatedVersionException;
import me.filoghost.holographicdisplays.core.NMSVersion.UnknownVersionException;
import me.filoghost.holographicdisplays.core.api.current.APIHologramManager;
import me.filoghost.holographicdisplays.core.api.current.DefaultHolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.core.api.v2.V2HologramManager;
import me.filoghost.holographicdisplays.core.api.v2.V2HologramsAPIProvider;
import me.filoghost.holographicdisplays.core.base.BaseHologram;
import me.filoghost.holographicdisplays.core.listener.ChunkListener;
import me.filoghost.holographicdisplays.core.listener.LineClickListener;
import me.filoghost.holographicdisplays.core.listener.PlayerListener;
import me.filoghost.holographicdisplays.core.placeholder.registry.PlaceholderRegistry;
import me.filoghost.holographicdisplays.core.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.core.tick.TickClock;
import me.filoghost.holographicdisplays.core.tick.TickingTask;
import me.filoghost.holographicdisplays.core.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.nms.common.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HolographicDisplaysCore {

    private NMSManager nmsManager;
    private LineTrackerManager lineTrackerManager;
    private APIHologramManager apiHologramManager;
    private V2HologramManager v2HologramManager;

    public void enable(Plugin plugin, ErrorCollector errorCollector) throws PluginEnableException {
        try {
            nmsManager = NMSVersion.getCurrent().createNMSManager(errorCollector);
        } catch (UnknownVersionException e) {
            throw new PluginEnableException("Holographic Displays only supports Spigot from 1.8 to 1.20.");
        } catch (OutdatedVersionException e) {
            throw new PluginEnableException("Holographic Displays only supports " + e.getMinimumSupportedVersion() + " and above.");
        } catch (Throwable t) {
            throw new PluginEnableException(t, "Couldn't initialize the NMS manager.");
        }

        PlaceholderRegistry placeholderRegistry = new PlaceholderRegistry();
        TickClock tickClock = new TickClock();
        ActivePlaceholderTracker placeholderTracker = new ActivePlaceholderTracker(placeholderRegistry, tickClock);
        LineClickListener lineClickListener = new LineClickListener();
        lineTrackerManager = new LineTrackerManager(nmsManager, placeholderTracker, lineClickListener);
        apiHologramManager = new APIHologramManager(lineTrackerManager);
        v2HologramManager = new V2HologramManager(lineTrackerManager);

        // Add packet listener for players currently online
        for (Player player : Bukkit.getOnlinePlayers()) {
            nmsManager.injectPacketListener(player, lineClickListener);
        }

        // Tasks
        TickingTask tickingTask = new TickingTask(tickClock, placeholderTracker, lineTrackerManager, lineClickListener);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, tickingTask, 0, 1);

        // Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListener(nmsManager, lineClickListener, tickingTask), plugin);
        Bukkit.getPluginManager().registerEvents(new ChunkListener(plugin, apiHologramManager, v2HologramManager), plugin);

        // Enable the APIs
        HolographicDisplaysAPIProvider.setImplementation(
                new DefaultHolographicDisplaysAPIProvider(apiHologramManager, placeholderRegistry));
        enableLegacyAPI(v2HologramManager, placeholderRegistry);
    }

    @SuppressWarnings("deprecation")
    private void enableLegacyAPI(V2HologramManager hologramManager, PlaceholderRegistry placeholderRegistry) {
        HologramsAPIProvider.setImplementation(new V2HologramsAPIProvider(hologramManager, placeholderRegistry));
    }

    public void setSpaceBetweenHologramLines(double spaceBetweenLines) {
        CoreGlobalConfig.spaceBetweenLines = spaceBetweenLines;
        for (BaseHologram hologram : apiHologramManager.getHolograms()) {
            hologram.getLines().updatePositions();
        }
        for (BaseHologram hologram : v2HologramManager.getHolograms()) {
            hologram.getLines().updatePositions();
        }
    }

    public void setMaxViewRange(int maxViewRange) {
        CoreGlobalConfig.maxViewRange = maxViewRange;
    }

    public void disable() {
        if (lineTrackerManager != null) {
            lineTrackerManager.resetViewersAndSendDestroyPackets();
        }

        if (nmsManager != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                nmsManager.uninjectPacketListener(player);
            }
        }
    }

}
