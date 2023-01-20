/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.listener;

import me.filoghost.holographicdisplays.api.hologram.line.HologramClickType;
import me.filoghost.holographicdisplays.core.tracking.ClickableLineTracker;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import me.filoghost.holographicdisplays.nms.common.PacketListener;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LineClickListener implements PacketListener {

    private final ConcurrentMap<Integer, ClickableLineTracker<?>> lineTrackerByEntityID;

    // It is necessary to queue async click events to process them from the main thread.
    // Use a set to avoid duplicate click events to the same line.
    private final Set<QueuedClickEvent> queuedClickEvents;

    public LineClickListener() {
        lineTrackerByEntityID = new ConcurrentHashMap<>();
        queuedClickEvents = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Override
    public boolean onAsyncEntityInteract(Player player, int entityID, boolean isRightClick) {
        ClickableLineTracker<?> lineTracker = lineTrackerByEntityID.get(entityID);
        if (lineTracker != null) {
            queuedClickEvents.add(new QueuedClickEvent(player, lineTracker, isRightClick ? HologramClickType.RIGHT_CLICK : HologramClickType.LEFT_CLICK));
            return true;
        } else {
            return false;
        }
    }

    // This method is called from the main thread
    public void processQueuedClickEvents() {
        for (QueuedClickEvent event : queuedClickEvents) {
            event.lineTracker.onClientClick(event.player, event.clickType);
        }
        queuedClickEvents.clear();
    }

    public void addLineTracker(EntityID clickableEntityID, ClickableLineTracker<?> lineTracker) {
        lineTrackerByEntityID.put(clickableEntityID.getNumericID(), lineTracker);
    }

    public void removeLineTracker(EntityID clickableEntityID) {
        if (clickableEntityID.hasInitializedNumericID()) {
            lineTrackerByEntityID.remove(clickableEntityID.getNumericID());
        }
    }


    private static class QueuedClickEvent {

        private final HologramClickType clickType;
        private final Player player;
        private final ClickableLineTracker<?> lineTracker;

        QueuedClickEvent(Player player, ClickableLineTracker<?> lineTracker, HologramClickType clickType) {
            this.player = player;
            this.clickType = clickType;
            this.lineTracker = lineTracker;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            QueuedClickEvent other = (QueuedClickEvent) obj;
            return this.player.equals(other.player) && this.lineTracker.equals(other.lineTracker);
        }

        @Override
        public int hashCode() {
            int result = player.hashCode();
            result = 31 * result + lineTracker.hashCode();
            return result;
        }

    }

}
