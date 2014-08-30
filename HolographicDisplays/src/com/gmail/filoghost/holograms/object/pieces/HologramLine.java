package com.gmail.filoghost.holograms.object.pieces;

import org.bukkit.World;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.HologramHorse;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.object.HologramBase;

import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

public class HologramLine extends FloatingDoubleEntity {
	
	private static final double VERTICAL_OFFSET = 54.56;

	private String text;
	private HologramHorse horse;
	private HologramWitherSkull skull;
	
	public HologramLine(String text) {
		this.text = text;
	}
	
	public HologramHorse getHorse() {
		return horse;
	}
	
	@Override
	public void spawn(HologramBase parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException {
		despawn();
		
		horse = nmsManager.spawnHologramHorse(bukkitWorld, x, y + VERTICAL_OFFSET, z, parent);
		skull = nmsManager.spawnHologramWitherSkull(bukkitWorld, x, y + VERTICAL_OFFSET, z, parent);

		// Let the horse ride the wither skull.
		skull.setPassengerNMS(horse);
		
		if (text.length() > 0) {
			horse.forceSetCustomName(text);
		}
		
		horse.setLockTick(true);
		skull.setLockTick(true);
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
	}

	@Override
	public void teleport(double x, double y, double z) {
		if (skull != null) {
			skull.setLocationNMS(x, y + VERTICAL_OFFSET, z);
			skull.sendUpdatePacketNear();
		}
	}
}
