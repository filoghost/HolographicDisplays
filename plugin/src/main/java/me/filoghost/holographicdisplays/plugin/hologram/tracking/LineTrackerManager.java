/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.common.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class LineTrackerManager {

    private final NMSManager nmsManager;
    private final PlaceholderTracker placeholderTracker;
    private final Collection<LineTracker<?>> lineTrackers;

    public LineTrackerManager(NMSManager nmsManager, PlaceholderTracker placeholderTracker) {
        this.nmsManager = nmsManager;
        this.placeholderTracker = placeholderTracker;
        this.lineTrackers = new LinkedList<>();
    }

    public TextLineTracker startTracking(StandardTextLine line) {
        TextLineTracker tracker = new TextLineTracker(line, nmsManager, placeholderTracker);
        lineTrackers.add(tracker);
        return tracker;
    }

    public ItemLineTracker startTracking(StandardItemLine line) {
        ItemLineTracker tracker = new ItemLineTracker(line, nmsManager);
        lineTrackers.add(tracker);
        return tracker;
    }

    public void updateTrackersAndSendChanges() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

        Iterator<LineTracker<?>> iterator = lineTrackers.iterator();
        while (iterator.hasNext()) {
            LineTracker<?> lineTracker = iterator.next();

            // Remove deleted trackers, sending destroy packets to tracked players
            if (lineTracker.isDeleted()) {
                lineTracker.clearTrackedPlayers();
                iterator.remove();
                continue;
            }

            lineTracker.updateAndSendChanges(onlinePlayers);
        }
    }

    public void clearTrackedPlayers() {
        for (LineTracker<?> tracker : lineTrackers) {
            tracker.clearTrackedPlayers();
        }
    }

    public void onPlayerQuit(Player player) {
        for (LineTracker<?> tracker : lineTrackers) {
            tracker.removeTrackedPlayer(player);
        }
    }

    public void onChunkLoad(Chunk chunk) {
        for (LineTracker<?> tracker : lineTrackers) {
            tracker.onChunkLoad(chunk);
        }
    }

    public void onChunkUnload(Chunk chunk) {
        for (LineTracker<?> tracker : lineTrackers) {
            tracker.onChunkUnload(chunk);
        }
    }

}
