package com.gmail.filoghost.holograms.nms.interfaces;

import org.bukkit.entity.Entity;

public interface HologramArmorStand extends NameableEntityNMS {
	
	// Sets the passenger of this entity through NMS.
	public void setPassengerNMS(BasicEntityNMS passenger);
	
	// Sets the passenger of this entity through NMS.
	public void setPassengerNMS(Entity bukkitEntity);

}
