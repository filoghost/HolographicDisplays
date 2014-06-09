package com.gmail.filoghost.holograms.nms.interfaces;

import com.gmail.filoghost.holograms.object.CraftHologram;

// Represents an entity that is part of a hologram.
public interface HologramComponent extends BasicEntityNMS {
	
	// Returns the linked BaseMultiEntity. Can be null.
	public CraftHologram getParentHologram();
	
	// Sets the linked BaseMultiEntity.
	public void setParentHologram(CraftHologram hologram);
	
}
