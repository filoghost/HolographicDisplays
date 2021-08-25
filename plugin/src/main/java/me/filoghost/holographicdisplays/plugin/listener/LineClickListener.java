/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.PacketListener;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseClickableHologramLine;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LineClickListener implements PacketListener {

    private final ConcurrentMap<Integer, BaseClickableHologramLine> linesByEntityID;

    // It is necessary to queue async click events to process them from the main thread.
    // Use a set to avoid duplicate click events to the same line.
    private final Set<QueuedClickEvent> queuedClickEvents;

    public LineClickListener() {
        linesByEntityID = new ConcurrentHashMap<>();
        queuedClickEvents = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Override
    public boolean onAsyncEntityInteract(Player player, int entityID) {
        BaseClickableHologramLine line = linesByEntityID.get(entityID);
        if (line != null) {
            queuedClickEvents.add(new QueuedClickEvent(player, line));
            return true;
        } else {
            return false;
        }
    }

    // This method is called from the main thread
    public void processQueuedClickEvents() {
        for (QueuedClickEvent queuedClickEvent : queuedClickEvents) {
            queuedClickEvent.line.onClick(queuedClickEvent.player);
        }
        queuedClickEvents.clear();
    }

    public void registerLine(EntityID clickableEntityID, BaseClickableHologramLine line) {
        linesByEntityID.put(clickableEntityID.getNumericID(), line);
    }

    public void unregisterLine(EntityID clickableEntityID) {
        if (clickableEntityID.hasInitializedNumericID()) {
            linesByEntityID.remove(clickableEntityID.getNumericID());
        }
    }


    private static class QueuedClickEvent {

        private final Player player;
        private final BaseClickableHologramLine line;

        QueuedClickEvent(Player player, BaseClickableHologramLine line) {
            this.player = player;
            this.line = line;
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
            return this.player.equals(other.player) && this.line.equals(other.line);
        }

        @Override
        public int hashCode() {
            int result = player.hashCode();
            result = 31 * result + line.hashCode();
            return result;
        }

    }

}
