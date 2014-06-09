package com.gmail.filoghost.holograms.nms.interfaces;

public interface BasicEntityNMS {

	// Locks the tick of the entities.
	public void setLockTick(boolean lock);
	
	// Kills the entity through NMS.
	public void killEntityNMS();
	
	// Sets the location through NMS.
	public void setLocationNMS(double x, double y, double z);
	
	// Returns if the entity is dead through NMS.
	public boolean isDeadNMS();

}
