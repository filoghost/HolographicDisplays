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
    private final LineTouchListener lineTouchListener;
    private final Collection<LineTracker<?>> lineTrackers;

    public LineTrackerManager(NMSManager nmsManager, PlaceholderTracker placeholderTracker, LineTouchListener lineTouchListener) {
        this.nmsManager = nmsManager;
        this.placeholderTracker = placeholderTracker;
        this.lineTouchListener = lineTouchListener;
        this.lineTrackers = new LinkedList<>();
    }

    public TextLineTracker startTracking(StandardTextLine line) {
        TextLineTracker tracker = new TextLineTracker(line, nmsManager, lineTouchListener, placeholderTracker);
        lineTrackers.add(tracker);
        return tracker;
    }

    public ItemLineTracker startTracking(StandardItemLine line) {
        ItemLineTracker tracker = new ItemLineTracker(line, nmsManager, lineTouchListener);
        lineTrackers.add(tracker);
        return tracker;
    }

    public void updateTrackersAndSendPackets() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

        Iterator<LineTracker<?>> iterator = lineTrackers.iterator();
        while (iterator.hasNext()) {
            LineTracker<?> lineTracker = iterator.next();

            // Remove deleted trackers, sending destroy packets to tracked players
            if (lineTracker.shouldBeRemoved()) {
                iterator.remove();
                lineTracker.onRemoval();
                continue;
            }

            lineTracker.updateAndSendPackets(onlinePlayers);
        }
    }

    public void clearTrackedPlayersAndSendPackets() {
        for (LineTracker<?> tracker : lineTrackers) {
            tracker.clearTrackedPlayersAndSendPackets();
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
