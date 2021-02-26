/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.event;

import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InternalHologramEditEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final InternalHologram internalHologram;
    
    public InternalHologramEditEvent(InternalHologram internalHologram) {
        this.internalHologram = internalHologram;
    }

    public InternalHologram getInternalHologram() {
        return internalHologram;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
