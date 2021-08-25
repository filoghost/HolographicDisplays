/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.event;

import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InternalHologramChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final InternalHologram hologram;
    private final ChangeType changeType;

    public InternalHologramChangeEvent(InternalHologram hologram, ChangeType changeType) {
        this.hologram = hologram;
        this.changeType = changeType;
    }

    public InternalHologram getHologram() {
        return hologram;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    public enum ChangeType {

        CREATE,
        EDIT_LINES,
        EDIT_POSITION,
        DELETE

    }

}
