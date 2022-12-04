/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.core.base.BaseItemHologramLine;
import me.filoghost.holographicdisplays.core.base.BaseTextHologramLine;
import me.filoghost.holographicdisplays.core.base.EditableHologramLine;
import me.filoghost.holographicdisplays.core.listener.LineClickListener;
import me.filoghost.holographicdisplays.core.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import me.filoghost.holographicdisplays.nms.common.NMSManager;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LineTrackerManager {

    private final NMSManager nmsManager;
    private final ActivePlaceholderTracker placeholderTracker;
    private final LineClickListener lineClickListener;
    private final Collection<LineTracker<?>> lineTrackers;

    public LineTrackerManager(
            NMSManager nmsManager,
            ActivePlaceholderTracker placeholderTracker,
            LineClickListener lineClickListener) {
        this.nmsManager = nmsManager;
        this.placeholderTracker = placeholderTracker;
        this.lineClickListener = lineClickListener;
        this.lineTrackers = new LinkedList<>();
    }

    public <T extends EditableHologramLine> void startTracking(T line) {
        if (line instanceof BaseTextHologramLine) {
            lineTrackers.add(new TextLineTracker((BaseTextHologramLine) line, nmsManager, lineClickListener, placeholderTracker));
        } else if (line instanceof BaseItemHologramLine) {
            lineTrackers.add(new ItemLineTracker((BaseItemHologramLine) line, nmsManager, lineClickListener));
        } else {
            throw new UnsupportedOperationException("unsupported line class: " + line.getClass().getName());
        }
    }

    public void update(List<CachedPlayer> onlinePlayers, List<CachedPlayer> movedPlayers, int maxViewRange) {
        Iterator<LineTracker<?>> iterator = lineTrackers.iterator();
        while (iterator.hasNext()) {
            LineTracker<?> lineTracker = iterator.next();

            // Remove deleted trackers
            if (lineTracker.shouldBeRemoved()) {
                iterator.remove();
                lineTracker.onRemoval();
                continue;
            }

            lineTracker.update(onlinePlayers, movedPlayers, maxViewRange);
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
