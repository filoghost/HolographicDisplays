package com.gmail.filoghost.holographicdisplays.object.line;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.util.MinecraftVersion;
import com.gmail.filoghost.holographicdisplays.util.Offsets;
import com.gmail.filoghost.holographicdisplays.util.Validator;

public class CraftItemLine extends CraftTouchableLine implements ItemLine {

	private ItemStack itemStack;
	private PickupHandler pickupHandler;
	
	private NMSItem nmsItem;
	private NMSEntityBase nmsVehicle;
	
	public CraftItemLine(CraftHologram parent, ItemStack itemStack) {
		super(0.7, parent);
		setItemStack(itemStack);
	}
	
	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public void setItemStack(ItemStack itemStack) {
		Validator.notNull(itemStack, "itemStack");
		Validator.isTrue(itemStack.getType() != Material.AIR, "itemStack's material cannot be AIR");
		this.itemStack = itemStack;
		
		if (nmsItem != null) {
			nmsItem.setItemStackNMS(itemStack);
		}
	}

	@Override
	public PickupHandler getPickupHandler() {
		return pickupHandler;
	}

	@Override
	public void setPickupHandler(PickupHandler pickupHandler) {
		this.pickupHandler = pickupHandler;
	}
	
	public void setTouchHandler(TouchHandler touchHandler) {
		
		if (nmsItem != null) {
			
			Location loc = nmsItem.getBukkitEntityNMS().getLocation();
			
			if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_9)) {
				super.setTouchHandler(touchHandler, loc.getWorld(), loc.getX(), loc.getY() - getItemOffset(), loc.getZ());
			} else if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
				super.setTouchHandler(touchHandler, loc.getWorld(), loc.getX(), loc.getY() - getItemOffset(), loc.getZ());
			} else {
				super.setTouchHandler(touchHandler, loc.getWorld(), loc.getX(), loc.getY() - getItemOffset(), loc.getZ());
			}
			
		} else {
			super.setTouchHandler(touchHandler, null, 0, 0, 0);
		}
	}

	@Override
	public void spawn(World world, double x, double y, double z) {
		super.spawn(world, x, y, z);
		
		if (itemStack != null && itemStack.getType() != Material.AIR) {
			
			double offset = getItemOffset();
			
			nmsItem = HolographicDisplays.getNMSManager().spawnNMSItem(world, x, y + offset, z, this, itemStack);
			
			if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
				nmsVehicle = HolographicDisplays.getNMSManager().spawnNMSArmorStand(world, x, y + offset, z, this);
			} else {
				nmsVehicle = HolographicDisplays.getNMSManager().spawnNMSWitherSkull(world, x, y + offset, z, this);
			}
			
			nmsItem.setPassengerOfNMS(nmsVehicle);
			
			nmsItem.setLockTick(true);
			nmsVehicle.setLockTick(true);
		}
	}

	
	@Override
	public void despawn() {
		super.despawn();
		
		if (nmsVehicle != null) {
			nmsVehicle.killEntityNMS();
			nmsVehicle = null;
		}
		
		if (nmsItem != null) {
			nmsItem.killEntityNMS();
			nmsItem = null;
		}
	}

	@Override
	public void teleport(double x, double y, double z) {
		super.teleport(x, y, z);
		
		double offset = getItemOffset();
		
		if (nmsVehicle != null) {
			nmsVehicle.setLocationNMS(x, y + offset, z);
		}
		
		if (nmsItem != null) {
			nmsItem.setLocationNMS(x, y + offset, z);
		}
	}

	@Override
	public int[] getEntitiesIDs() {
		if (isSpawned()) {
			if (touchSlime != null) {
				return ArrayUtils.addAll(new int[] {nmsVehicle.getIdNMS(), nmsItem.getIdNMS()}, touchSlime.getEntitiesIDs());
			} else {
				return new int[] {nmsVehicle.getIdNMS(), nmsItem.getIdNMS()};
			}
		} else {
			return new int[0];
		}
	}

	public NMSItem getNmsItem() {
		return nmsItem;
	}

	public NMSEntityBase getNmsVehicle() {
		return nmsVehicle;
	}
	
	private double getItemOffset() {
		if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_9)) {
			return Offsets.ARMOR_STAND_WITH_ITEM_1_9;
		} else if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
			return Offsets.ARMOR_STAND_WITH_ITEM;
		} else {
			return Offsets.WITHER_SKULL_WITH_ITEM;
		}
	}

	@Override
	public String toString() {
		return "CraftItemLine [itemStack=" + itemStack + ", pickupHandler=" + pickupHandler + "]";
	}
	
}
