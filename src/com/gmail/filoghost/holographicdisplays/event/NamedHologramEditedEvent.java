package com.gmail.filoghost.holographicdisplays.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.filoghost.holographicdisplays.object.NamedHologram;

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
