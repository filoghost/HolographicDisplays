package com.gmail.filoghost.holographicdisplays.api.line;

import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;

/**
 * A piece of hologram that can be picked up.
 */
public interface CollectableLine extends HologramLine {

	public void setPickupHandler(PickupHandler pickupHandler);
	
	public PickupHandler getPickupHandler();
	
}
