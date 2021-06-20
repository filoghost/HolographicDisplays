/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.event;

import me.filoghost.holographicdisplays.plugin.object.internal.InternalHologram;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InternalHologramEditEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final InternalHologram hologram;
    
    public InternalHologramEditEvent(InternalHologram hologram) {
        this.hologram = hologram;
    }

    public InternalHologram getHologram() {
        return hologram;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
