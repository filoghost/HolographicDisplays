package com.gmail.filoghost.holograms.object.pieces;

import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

import org.bukkit.World;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.HologramArmorStand;
import com.gmail.filoghost.holograms.nms.interfaces.HologramHorse;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.nms.interfaces.NameableEntityNMS;
import com.gmail.filoghost.holograms.object.HologramBase;

public class HologramLine extends FloatingDoubleEntity {
	
	private static final double VERTICAL_OFFSET_SKULL = 54.56;
	private static final double VERTICAL_OFFSET_ARMORSTAND = -1.25;

	private String text;
	private HologramHorse horse;
	private HologramWitherSkull skull;
	private HologramArmorStand armorStand;
	
	public HologramLine(String text) {
		this.text = text;
	}
	
	public NameableEntityNMS getTextEntity() {
		if (HolographicDisplays.is1_8) {
			return armorStand;
		} else {
			return horse;
		}
	}
	
	@Override
	public void spawn(HologramBase parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException {
		despawn();
		
		if (HolographicDisplays.is1_8) {
			
			armorStand = nmsManager.spawnHologramArmorStand(bukkitWorld, x, y + VERTICAL_OFFSET_ARMORSTAND, z, parent);
			
			if (text.length() > 0) {
				armorStand.forceSetCustomName(text);
			}
			
			armorStand.setLockTick(true);
			
		} else {
			horse = nmsManager.spawnHologramHorse(bukkitWorld, x, y + VERTICAL_OFFSET_SKULL, z, parent);
			skull = nmsManager.spawnHologramWitherSkull(bukkitWorld, x, y + VERTICAL_OFFSET_SKULL, z, parent);

			// Let the horse ride the wither skull.
			skull.setPassengerNMS(horse);
		
			if (text.length() > 0) {
				horse.forceSetCustomName(text);
			}
		
			horse.setLockTick(true);
			skull.setLockTick(true);
		}
	}

	@Override
	public void despawn() {
		if (horse != null) {
			horse.killEntityNMS();
			horse = null;
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
