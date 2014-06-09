package com.gmail.filoghost.holograms.object;

import org.bukkit.World;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.nms.interfaces.TouchSlime;
import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

public class FloatingTouchSlime extends FloatingDoubleEntity {

	private static final double VERTICAL_OFFSET = -0.3;

	private TouchSlime slime;
	private HologramWitherSkull skull;
	
	public FloatingTouchSlime() {
	}
	
	@Override
	public void spawn(CraftHologram parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException {
		despawn();
		
		slime = nmsManager.spawnTouchSlime(bukkitWorld, x, y + VERTICAL_OFFSET, z);
		slime.setParentHologram(parent);
		
		skull = nmsManager.spawnHologramWitherSkull(bukkitWorld, x, y + VERTICAL_OFFSET, z);
		skull.setParentHologram(parent);
		
		// Let the slime ride the wither skull.
		skull.setPassengerNMS(slime);

		slime.setLockTick(true);
		skull.setLockTick(true);
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
	}
	
	public boolean isSpawned() {
		return slime != null && skull != null;
	}
}
