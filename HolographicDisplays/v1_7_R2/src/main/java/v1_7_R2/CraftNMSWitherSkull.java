package com.gmail.filoghost.holographicdisplays.nms.v1_7_R2;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftServer;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftWitherSkull;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

public class CraftNMSWitherSkull extends CraftWitherSkull {

	public CraftNMSWitherSkull(CraftServer server, EntityNMSWitherSkull entity) {
		super(server, entity);
	}

	// Disallow all the bukkit methods.
	
	@Override
	public void remove() {
		// Cannot be removed, this is the most important to override.
	}
		
	// Method from Fireball
	@Override public void setDirection(Vector dir) { }

	// Method from Projectile
	@Override public void setBounce(boolean bounce) { }
		
	// Methods from Explosive
	@Override public void setYield(float yield) { }
	@Override public void setIsIncendiary(boolean fire) { }
		
	// Methods from Entity
	@Override public void setVelocity(Vector vel) { }
	@Override public boolean teleport(Location loc) { return false; }
	@Override public boolean teleport(Entity entity) { return false; }
	@Override public boolean teleport(Location loc, TeleportCause cause) { return false; }
	@Override public boolean teleport(Entity entity, TeleportCause cause) { return false; }
	@Override public void setFireTicks(int ticks) { }
	@Override public boolean setPassenger(Entity entity) { return false; }
	@Override public boolean eject() { return false; }
	@Override public boolean leaveVehicle() { return false; }
	@Override public void playEffect(EntityEffect effect) { }

}
