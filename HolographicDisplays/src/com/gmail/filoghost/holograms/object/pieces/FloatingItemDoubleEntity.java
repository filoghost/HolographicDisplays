package com.gmail.filoghost.holograms.object.pieces;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.CustomItem;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.object.HologramBase;

import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

public class FloatingItemDoubleEntity extends FloatingDoubleEntity {

	private static final double VERTICAL_OFFSET = -0.21;
	
	private ItemStack itemStack;
	private CustomItem item;
	private HologramWitherSkull skull;
	
	private boolean allowPickup;
	
	public FloatingItemDoubleEntity(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	@Override
	public void spawn(HologramBase parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException {
		despawn();
		
		item = nmsManager.spawnCustomItem(bukkitWorld, x, y + VERTICAL_OFFSET, z, parent, itemStack);		
		skull = nmsManager.spawnHologramWitherSkull(bukkitWorld, x, y + VERTICAL_OFFSET, z, parent);
		
		item.allowPickup(allowPickup);
		
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
	
	public boolean isSpawned() {
		return item != null && skull != null;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		if (isSpawned()) {
			item.setItemStackNMS(itemStack);
		}
	}
	
	public void setAllowPickup(boolean allowPickup) {
		this.allowPickup = allowPickup;
		if (item != null) {
			item.allowPickup(allowPickup);
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
