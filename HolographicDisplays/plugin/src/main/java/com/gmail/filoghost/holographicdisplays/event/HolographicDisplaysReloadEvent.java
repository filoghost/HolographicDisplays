package com.gmail.filoghost.holographicdisplays.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HolographicDisplaysReloadEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
