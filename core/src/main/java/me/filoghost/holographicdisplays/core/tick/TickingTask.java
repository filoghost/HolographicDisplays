/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tick;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.core.CoreGlobalConfig;
import me.filoghost.holographicdisplays.core.NMSVersion;
import me.filoghost.holographicdisplays.core.listener.LineClickListener;
import me.filoghost.holographicdisplays.core.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.core.tracking.LineTrackerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TickingTask implements Runnable {

    private final TickClock tickClock;
    private final ActivePlaceholderTracker placeholderTracker;
    private final LineTrackerManager lineTrackerManager;
    private final LineClickListener lineClickListener;
    private final List<CachedPlayer> onlinePlayers;

    private long lastErrorLogTick;

    public TickingTask(
            TickClock tickClock,
            ActivePlaceholderTracker placeholderTracker,
            LineTrackerManager lineTrackerManager,
            LineClickListener lineClickListener) {
        this.tickClock = tickClock;
        this.placeholderTracker = placeholderTracker;
        this.lineTrackerManager = lineTrackerManager;
        this.lineClickListener = lineClickListener;
        this.onlinePlayers = new ArrayList<>();
    }

    public void onPlayerJoin(Player player) {
        onlinePlayers.add(new CachedPlayer(player));
    }

    public void onPlayerQuit(Player player) {
        Iterator<CachedPlayer> iterator = onlinePlayers.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getBukkitPlayer() == player) {
                iterator.remove();
                break;
            }
        }

        lineTrackerManager.removeViewer(player);
    }

    public void onPlayerRespawn(Player player) {
        switch (NMSVersion.getCurrent()) {
            case v1_8_R1:
            case v1_8_R2:
            case v1_8_R3:
            case v1_9_R1:
            case v1_9_R2:
            case v1_10_R1:
            case v1_11_R1:
            case v1_12_R1:
            case v1_13_R1:
            case v1_13_R2:
            case v1_14_R1:
                // For older versions, force spawn packets to be resent after the player respawns
                lineTrackerManager.removeViewer(player);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        tickClock.incrementTick();

        // Remove outdated entries before using them from line trackers
        placeholderTracker.clearOutdatedEntries();

        List<CachedPlayer> movedPlayers = new ArrayList<>();
        for (CachedPlayer onlinePlayer : onlinePlayers) {
            if (onlinePlayer.isMovedLastTick()) {
                movedPlayers.add(onlinePlayer);
            }
        }

        // Holograms need to disappear before chunks (code taken from Bukkit)
        int maxViewRange = (Bukkit.getViewDistance() - 1) * 16;
        if (maxViewRange > CoreGlobalConfig.maxViewRange) {
            maxViewRange = CoreGlobalConfig.maxViewRange;
        }

        try {
            lineTrackerManager.update(onlinePlayers, movedPlayers, maxViewRange);
        } catch (Throwable t) {
            // Catch all types of Throwable because we're using NMS code
            if (tickClock.getCurrentTick() - lastErrorLogTick >= 20) {
                // Avoid spamming the console, log the error at most once every 20 ticks
                lastErrorLogTick = tickClock.getCurrentTick();
                Log.severe("Error while ticking holograms", t);
            }
        }

        // Remove placeholders which were not used by line trackers
        placeholderTracker.clearInactivePlaceholders();

        // Invoke click listeners
        lineClickListener.processQueuedClickEvents();

        // Delay position updates, so that they will be handled next tick.
        // A tick of delay is necessary to avoid issues such as holograms not being visible after a teleport.
        for (CachedPlayer onlinePlayer : onlinePlayers) {
            onlinePlayer.onTick();
        }
    }

}
