/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextLine;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class LineTrackerManager {

    private final NMSManager nmsManager;
    private final PlaceholderTracker placeholderTracker;
    private final LineClickListener lineClickListener;
    private final Collection<LineTracker<?>> lineTrackers;

    public LineTrackerManager(NMSManager nmsManager, PlaceholderTracker placeholderTracker, LineClickListener lineClickListener) {
        this.nmsManager = nmsManager;
        this.placeholderTracker = placeholderTracker;
        this.lineClickListener = lineClickListener;
        this.lineTrackers = new LinkedList<>();
    }

    public TextLineTracker startTracking(BaseTextLine line) {
        TextLineTracker tracker = new TextLineTracker(line, nmsManager, lineClickListener, placeholderTracker);
        lineTrackers.add(tracker);
        return tracker;
    }

    public ItemLineTracker startTracking(BaseItemLine line) {
        ItemLineTracker tracker = new ItemLineTracker(line, nmsManager, lineClickListener);
        lineTrackers.add(tracker);
        return tracker;
    }

    public void update() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

        Iterator<LineTracker<?>> iterator = lineTrackers.iterator();
        while (iterator.hasNext()) {
            LineTracker<?> lineTracker = iterator.next();

            // Remove deleted trackers
            if (lineTracker.shouldBeRemoved()) {
                iterator.remove();
                lineTracker.onRemoval();
                continue;
            }

            lineTracker.update(onlinePlayers);
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

}
