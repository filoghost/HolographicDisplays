package com.gmail.filoghost.holograms.object;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.api.ItemTouchHandler;
import com.gmail.filoghost.holograms.api.PickupHandler;
import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.object.pieces.FloatingItemDoubleEntity;
import com.gmail.filoghost.holograms.object.pieces.FloatingTouchSlimeDoubleEntity;
import com.gmail.filoghost.holograms.utils.Validator;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */

public class CraftFloatingItem extends HologramBase implements FloatingItem {
	
	protected FloatingItemDoubleEntity floatingItemDoubleEntity;
	
	protected long creationTimestamp;
	
	protected FloatingTouchSlimeDoubleEntity touchSlimeEntity;
	protected ItemTouchHandler touchHandler;
	protected PickupHandler pickupHandler;

	public CraftFloatingItem(Location source, ItemStack itemStack) {
		super("{API-FloatingItem}", source);
		floatingItemDoubleEntity = new FloatingItemDoubleEntity(itemStack);
		touchSlimeEntity = new FloatingTouchSlimeDoubleEntity();
		creationTimestamp = System.currentTimeMillis();
	}
	
	@Override
	public long getCreationTimestamp() {
		return creationTimestamp;
	}
	
	@Override
	public void setTouchHandler(ItemTouchHandler touchHandler) {
		this.touchHandler = touchHandler;
		
		if (touchHandler != null && !touchSlimeEntity.isSpawned()) {
			try {
				touchSlimeEntity.spawn(this, bukkitWorld, x, y, z);
			} catch (SpawnFailedException e) { }
		} else if (touchHandler == null && touchSlimeEntity.isSpawned()) {
			touchSlimeEntity.despawn();
		}
	}

	@Override
	public boolean hasTouchHandler() {
		return touchHandler != null;
	}

	@Override
	public ItemTouchHandler getTouchHandler() {
		return touchHandler;
	}
	
	@Override
	public void setPickupHandler(PickupHandler handler) {
		this.pickupHandler = handler;
		
		// Allow the item to be pickup up only if there's a PickupHandler.
		floatingItemDoubleEntity.setAllowPickup(hasPickupHandler());
	}

	@Override
	public PickupHandler getPickupHandler() {
		return pickupHandler;
	}

	@Override
	public boolean hasPickupHandler() {
		return pickupHandler != null;
	}

	@Override
	public boolean update() {
		if (isInLoadedChunk()) {
			return forceUpdate();
		}
		
		return true;
	}

	/**
	 *  Updates the hologram without checking if it's in a loaded chunk.
	 */
	public boolean forceUpdate() {
		
		Validator.checkState(!isDeleted(), "Floating item already deleted");
	
		// Remove previous entities.
		hide();
		
		try {
			
			floatingItemDoubleEntity.spawn(this, bukkitWorld, x, y, z);
			
		} catch (SpawnFailedException ex) {
			// Kill the entities and return false.
			hide();
			return false;
		}
		
		return true;
	}

	@Override
	public void hide() {
		floatingItemDoubleEntity.despawn();
		
		if (touchSlimeEntity.isSpawned()) {
			touchSlimeEntity.despawn();
		}
	}

	@Override
	public void onDeleteEvent() {
		hide();
		APIFloatingItemManager.remove(this);
	}
	
	@Override
	public String toString() {
		return "CraftFloatingItem{itemstack=" + floatingItemDoubleEntity.getItemStack().toString() + ",x=" + x + ",y=" + y + ",z=" + z + ",world=" + bukkitWorld.getName() + "}";
	}
	
	@Override
	public void teleport(Location loc) {
		Validator.notNull(loc, "location cannot be null");
		Validator.notNull(loc.getWorld(), "location's world cannot be null");
		
		if (loc.getWorld().equals(bukkitWorld) && loc.getChunk().isLoaded() && floatingItemDoubleEntity.isSpawned()) {
			
			/* Conditions:
			 * - Same world
			 * - Destination chunk is loaded
			 * - Entities for this floating item are already spawned
			 * 
			 * Then:
			 * Send a packet near to update the position, because of this bug: https://bugs.mojang.com/browse/MC-55638
			 */
			
			
			floatingItemDoubleEntity.teleport(loc.getX(), loc.getY(), loc.getZ());
			
			if (touchSlimeEntity.isSpawned()) {
				touchSlimeEntity.teleport(loc.getX(), loc.getY(), loc.getZ());
			}
			setLocation(loc);
			
		} else {
			
			boolean wasSpawned = floatingItemDoubleEntity.isSpawned();
			
			// Recreate it completely.
			hide();
			setLocation(loc);
			
			if (wasSpawned) {
				update();
			}
		}
	}

	@Override
	public void setItemStack(ItemStack itemstack) {
		Validator.notNull(itemstack, "itemStack cannot be null");
		Validator.checkArgument(itemstack.getType() != Material.AIR, "itemStack cannot be AIR");
		floatingItemDoubleEntity.setItemStack(itemstack);
	}

	@Override
	public ItemStack getItemStack() {
		return floatingItemDoubleEntity.getItemStack();
	}
}
