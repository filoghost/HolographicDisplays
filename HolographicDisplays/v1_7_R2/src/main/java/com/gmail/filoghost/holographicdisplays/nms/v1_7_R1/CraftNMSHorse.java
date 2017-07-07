package com.gmail.filoghost.holographicdisplays.nms.v1_7_R1;

import java.util.Collection;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class CraftNMSHorse extends CraftHorse {

	public CraftNMSHorse(CraftServer server, EntityNMSHorse entity) {
		super(server, entity);
	}
	
	// Disallow all the bukkit methods.
	
	@Override
	public void remove() {
		// Cannot be removed, this is the most important to override.
	}
	
	// Methods from Horse class
	@Override public void setVariant(Variant variant) { }
	@Override public void setColor(Color color) { }
	@Override public void setStyle(Style style) { }
	@Override public void setCarryingChest(boolean chest) { }
	@Override public void setDomestication(int domestication) { }
	@Override public void setJumpStrength(double jump) { }
	
	// Methods form Ageable class
	@Override public void setAge(int age) { }
	@Override public void setAgeLock(boolean lock) { }
	@Override public void setBreed(boolean breed) { }
	@Override public void setAdult() { }
	@Override public void setBaby() { }
	
	// Methods from LivingEntity class
	@Override public boolean addPotionEffect(PotionEffect effect) { return false; }
	@Override public boolean addPotionEffect(PotionEffect effect, boolean param) { return false; }
	@Override public boolean addPotionEffects(Collection<PotionEffect> effects) { return false; }
	@Override public void setRemoveWhenFarAway(boolean remove) { }
	
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
	
	// Methods from Tameable
	@Override public void setTamed(boolean tame) { }
	@Override public void setOwner(AnimalTamer owner) { }
}
