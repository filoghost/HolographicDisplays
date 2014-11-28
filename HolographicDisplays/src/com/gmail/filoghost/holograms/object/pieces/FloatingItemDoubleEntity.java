package com.gmail.filoghost.holograms.object.pieces;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.nms.interfaces.CustomItem;
import com.gmail.filoghost.holograms.nms.interfaces.HologramArmorStand;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.object.HologramBase;

import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

public class FloatingItemDoubleEntity extends FloatingDoubleEntity {

	private static final double VERTICAL_OFFSET_SKULL = -0.21;
	private static final double VERTICAL_OFFSET_ARMORSTAND = -1.48; //TODO
	
	private ItemStack itemStack;
	private CustomItem item;
	private HologramWitherSkull skull;
	private HologramArmorStand armorStand;
	
	private boolean allowPickup;
	
	public FloatingItemDoubleEntity(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	@Override
	public void spawn(HologramBase parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException {
		despawn();
		
		if (HolographicDisplays.is1_8) {
			
			item = nmsManager.spawnCustomItem(bukkitWorld, x, y + VERTICAL_OFFSET_ARMORSTAND, z, parent, itemStack);
			armorStand = nmsManager.spawnHologramArmorStand(bukkitWorld, x, y + VERTICAL_OFFSET_ARMORSTAND, z, parent);
			
			item.allowPickup(allowPickup);
			
			armorStand.setPassengerNMS(item);
			
			item.setLockTick(true);
			armorStand.setLockTick(true);
			
		} else {
		
			item = nmsManager.spawnCustomItem(bukkitWorld, x, y + VERTICAL_OFFSET_SKULL, z, parent, itemStack);
			skull = nmsManager.spawnHologramWitherSkull(bukkitWorld, x, y + VERTICAL_OFFSET_SKULL, z, parent);
			
			item.allowPickup(allowPickup);
			
			// Let the item ride the wither skull.
			skull.setPassengerNMS(item);
	
			item.setLockTick(true);
			skull.setLockTick(true);
		}
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
		
		if (armorStand != null) {
			armorStand.killEntityNMS();
			armorStand = null;
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
			skull.setLocationNMS(x, y + VERTICAL_OFFSET_SKULL, z);
			skull.sendUpdatePacketNear();
		}
		
		if (armorStand != null) {
			armorStand.setLocationNMS(x, y + VERTICAL_OFFSET_ARMORSTAND, z);
		}
	}
}
