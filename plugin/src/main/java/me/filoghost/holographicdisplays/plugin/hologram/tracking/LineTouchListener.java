/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.PacketListener;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTouchableLine;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LineTouchListener implements PacketListener {

    private final ConcurrentMap<Integer, BaseTouchableLine> linesByEntityID;

    // It is necessary to queue async touch events to process them from the main thread.
    // Use a set to avoid duplicate touch events to the same line.
    private final Set<TouchEvent> queuedTouchEvents;

    public LineTouchListener() {
        linesByEntityID = new ConcurrentHashMap<>();
        queuedTouchEvents = new HashSet<>();
    }

    @Override
    public boolean onAsyncEntityInteract(Player player, int entityID) {
        BaseTouchableLine line = linesByEntityID.get(entityID);
        if (line != null) {
            queuedTouchEvents.add(new TouchEvent(player, line));
            return true;
        } else {
            return false;
        }
    }

    public void processQueuedTouchEvents() {
        for (TouchEvent touchEvent : queuedTouchEvents) {
            touchEvent.line.onTouch(touchEvent.player);
        }
        queuedTouchEvents.clear();
    }

    public void registerLine(EntityID touchableEntityID, BaseTouchableLine line) {
        linesByEntityID.put(touchableEntityID.getNumericID(), line);
    }

    public void unregisterLine(EntityID touchableEntityID) {
        if (touchableEntityID.hasInitializedNumericID()) {
            linesByEntityID.remove(touchableEntityID.getNumericID());
        }
    }


    private static class TouchEvent {

        private final Player player;
        private final BaseTouchableLine line;

        TouchEvent(Player player, BaseTouchableLine line) {
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

            TouchEvent other = (TouchEvent) obj;

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
