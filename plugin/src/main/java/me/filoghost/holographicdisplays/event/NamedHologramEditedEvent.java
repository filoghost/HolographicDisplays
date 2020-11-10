/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.event;

import me.filoghost.holographicdisplays.object.NamedHologram;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NamedHologramEditedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private NamedHologram namedHologram;
    
    public NamedHologramEditedEvent(NamedHologram namedHologram) {
        this.namedHologram = namedHologram;
    }

    public NamedHologram getNamedHologram() {
        return namedHologram;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
