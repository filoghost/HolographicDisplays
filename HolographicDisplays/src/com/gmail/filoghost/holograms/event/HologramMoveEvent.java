package com.gmail.filoghost.holograms.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gmail.filoghost.holograms.api.Hologram;

/**
 * Called after that a hologram is moved with /hd movehere <hologram>
 */
public class HologramMoveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
    private Hologram hologram;
    
    public HologramMoveEvent(Hologram hologram) {
        this.hologram = hologram;
    }
    
    public Hologram getHologram() {
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
