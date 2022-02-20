/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.plugin.tick.CachedPlayer;
import me.filoghost.holographicdisplays.plugin.tick.TickClock;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LineTrackerManager {

    private final TickClock tickClock;
    private final NMSManager nmsManager;
    private final ActivePlaceholderTracker placeholderTracker;
    private final LineClickListener lineClickListener;
    private final Collection<LineTracker<?>> lineTrackers;

    public LineTrackerManager(
            NMSManager nmsManager,
            ActivePlaceholderTracker placeholderTracker,
            LineClickListener lineClickListener,
            TickClock tickClock) {
        this.nmsManager = nmsManager;
        this.placeholderTracker = placeholderTracker;
        this.lineClickListener = lineClickListener;
        this.lineTrackers = new LinkedList<>();
        this.tickClock = tickClock;
    }

    public TextLineTracker startTracking(BaseTextHologramLine line) {
        TextLineTracker tracker = new TextLineTracker(line, nmsManager, lineClickListener, placeholderTracker, tickClock);
        lineTrackers.add(tracker);
        return tracker;
    }

    public ItemLineTracker startTracking(BaseItemHologramLine line) {
        ItemLineTracker tracker = new ItemLineTracker(line, nmsManager, lineClickListener, tickClock);
        lineTrackers.add(tracker);
        return tracker;
    }

    public void update(List<CachedPlayer> onlinePlayers) {
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

    public void resetViewersAndSendDestroyPackets() {
        for (LineTracker<?> tracker : lineTrackers) {
            tracker.resetViewersAndSendDestroyPackets();
        }
    }

    public void removeViewer(Player player) {
        for (LineTracker<?> tracker : lineTrackers) {
            tracker.removeViewer(player);
        }
    }

}
