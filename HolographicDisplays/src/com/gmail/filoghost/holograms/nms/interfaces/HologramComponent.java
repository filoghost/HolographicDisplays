package com.gmail.filoghost.holograms.nms.interfaces;

import com.gmail.filoghost.holograms.object.HologramBase;

// Represents an entity that is part of a hologram.
public interface HologramComponent extends BasicEntityNMS {
	
	// Returns the linked HologramBase. Can be null.
	public HologramBase getParentHologram();
	
	// Sets the linked HologramBase.
	public void setParentHologram(HologramBase base);
	
}
