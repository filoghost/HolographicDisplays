package com.gmail.filoghost.holograms.object.pieces;

import org.bukkit.World;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.HologramArmorStand;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.nms.interfaces.TouchSlime;
import com.gmail.filoghost.holograms.object.HologramBase;

import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

public class FloatingTouchSlimeDoubleEntity extends FloatingDoubleEntity {

	private static final double VERTICAL_OFFSET_SKULL = -0.3;
	private static final double VERTICAL_OFFSET_ARMORSTAND = -1.6;

	private TouchSlime slime;
	private HologramWitherSkull skull;
	private HologramArmorStand armorStand;
	
	public FloatingTouchSlimeDoubleEntity() {
	}
	
	@Override
	public void spawn(HologramBase parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException {
		despawn();
		
		if (HolographicDisplays.is1_8) {
		
			slime = nmsManager.spawnTouchSlime(bukkitWorld, x, y + VERTICAL_OFFSET_ARMORSTAND, z, parent);
			armorStand = nmsManager.spawnHologramArmorStand(bukkitWorld, x, y + VERTICAL_OFFSET_ARMORSTAND, z, parent);
			
			armorStand.setPassengerNMS(slime);
			
			slime.setLockTick(true);
			armorStand.setLockTick(true);
			
		} else {
			slime = nmsManager.spawnTouchSlime(bukkitWorld, x, y + VERTICAL_OFFSET_SKULL, z, parent);
			skull = nmsManager.spawnHologramWitherSkull(bukkitWorld, x, y + VERTICAL_OFFSET_SKULL, z, parent);
			
			// Let the slime ride the wither skull.
			skull.setPassengerNMS(slime);
	
			slime.setLockTick(true);
			skull.setLockTick(true);
		}
	}

	@Override
	public void despawn() {
		if (slime != null) {
			slime.killEntityNMS();
			slime = null;
		}
		
		if (skull != null) {
			skull.killEntityNMS();
			skull = null;
		}
		
		if (armorStand != null) {
			armorStand.killEntityNMS();
			armorStand = null;
		}
	}
	
	public boolean isSpawned() {
		return slime != null && skull != null && armorStand != null;
	}
	
	@Override
	public void teleport(double x, double y, double z) {
		if (skull != null) {
			skull.setLocationNMS(x, y + VERTICAL_OFFSET_SKULL, z);
			skull.sendUpdatePacketNear();
		}
		
		if (armorStand != null) {
			armorStand.setLocationNMS(x, y + VERTICAL_OFFSET_ARMORSTAND, z);
		}
	}
}
