package com.gmail.filoghost.holographicdisplays.nms.v1_8_R3;

import java.util.Collection;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class CraftNMSArmorStand extends CraftArmorStand {

	public CraftNMSArmorStand(CraftServer server, EntityNMSArmorStand entity) {
		super(server, entity);
	}
	
	// Disallow all the bukkit methods.
	
	@Override
	public void remove() {
		// Cannot be removed, this is the most important to override.
	}

	// Methods from Armor stand class
	@Override public void setArms(boolean arms) { }
	@Override public void setBasePlate(boolean basePlate) { }
	@Override public void setBodyPose(EulerAngle pose) { }
	@Override public void setBoots(ItemStack item) { }
	@Override public void setChestplate(ItemStack item) { }
	@Override public void setGravity(boolean gravity) { }
	@Override public void setHeadPose(EulerAngle pose) { }
	@Override public void setHelmet(ItemStack item) { }
	@Override public void setItemInHand(ItemStack item) { }
	@Override public void setLeftArmPose(EulerAngle pose) { }
	@Override public void setLeftLegPose(EulerAngle pose) { }
	@Override public void setLeggings(ItemStack item) { }
	@Override public void setRightArmPose(EulerAngle pose) { }
	@Override public void setRightLegPose(EulerAngle pose) { }
	@Override public void setSmall(boolean small) { }
	@Override public void setVisible(boolean visible) { }

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
	@Override public void setCustomName(String name) { }
	@Override public void setCustomNameVisible(boolean flag) { }
	
}
