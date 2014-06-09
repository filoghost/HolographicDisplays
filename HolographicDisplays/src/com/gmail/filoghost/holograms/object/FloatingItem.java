package com.gmail.filoghost.holograms.object;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.CustomItem;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

public class FloatingItem extends FloatingDoubleEntity {

	private static final double VERTICAL_OFFSET = -0.21;
	
	private ItemStack itemStack;
	private CustomItem item;
	private HologramWitherSkull skull;
	
	public FloatingItem(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	@Override
	public void spawn(CraftHologram parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException {
		despawn();
		
		item = nmsManager.spawnCustomItem(bukkitWorld, x, y + VERTICAL_OFFSET, z, itemStack);
		item.setParentHologram(parent);
		
		skull = nmsManager.spawnHologramWitherSkull(bukkitWorld, x, y + VERTICAL_OFFSET, z);
		skull.setParentHologram(parent);
		
		// Let the item ride the wither skull.
		skull.setPassengerNMS(item);

		item.setLockTick(true);
		skull.setLockTick(true);
	}

	@Override
	public void despawn() {
		if (item != null) {
			item.killEntityNMS();
			item = null;
		}
		
		if (skull != null) {
			skull.killEntityNMS();
			skull = null;
		}
	}
}
