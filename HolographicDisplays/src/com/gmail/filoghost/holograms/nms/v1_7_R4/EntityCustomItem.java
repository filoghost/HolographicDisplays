package com.gmail.filoghost.holograms.nms.v1_7_R4;

import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityItem;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.api.FloatingItem;
import com.gmail.filoghost.holograms.nms.interfaces.BasicEntityNMS;
import com.gmail.filoghost.holograms.nms.interfaces.CustomItem;
import com.gmail.filoghost.holograms.object.HologramBase;

public class EntityCustomItem extends EntityItem implements CustomItem, BasicEntityNMS {
	
	private boolean lockTick;
	private HologramBase parent;
	
	public EntityCustomItem(World world) {
		super(world);
		super.pickupDelay = Integer.MAX_VALUE;
	}
	
	@Override
	public void h() {		
		// Checks every 20 ticks.
		if (ticksLived % 20 == 0) {
			// The item dies without a vehicle.
			if (this.vehicle == null) {
				die();
			}
		}
		
		if (!lockTick) {
			super.h();
		}
	}
	
	@Override
	public ItemStack getItemStack() {
		// Dirty method to check if the icon is being picked up
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if (stacktrace.length > 2 && stacktrace[2].getClassName().contains("EntityInsentient")) {
			return null; // Try to pickup this, dear entity ignoring the pickupDelay!
		}
		
		return super.getItemStack();
	}
	
	// Method called when a player is near.
	@Override
	public void b_(EntityHuman human) {
		
		if (parent instanceof FloatingItem && human instanceof EntityPlayer) {

			FloatingItem floatingItemParent = (FloatingItem) parent;
			if (floatingItemParent.hasPickupHandler()) {
				floatingItemParent.getPickupHandler().onPickup(floatingItemParent, (Player) human.getBukkitEntity());
			}
			
			// It is never added to the inventory.
		}
	}
	
	@Override
	public void b(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}
	
	@Override
	public boolean c(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public boolean d(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}
	
	@Override
	public void e(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}
	
	@Override
	public boolean isInvulnerable() {
		/* 
		 * The field Entity.invulnerable is private.
		 * It's only used while saving NBTTags, but since the entity would be killed
		 * on chunk unload, we prefer to override isInvulnerable().
		 */
	    return true;
	}

	@Override
	public void setLockTick(boolean lock) {
		lockTick = lock;
	}
	
	@Override
	public void die() {
		setLockTick(false);
		super.die();
	}

	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new CraftCustomItem(this.world.getServer(), this);
	    }
		return this.bukkitEntity;
	}

	@Override
	public boolean isDeadNMS() {
		return this.dead;
	}
	
	@Override
	public void killEntityNMS() {
		die();
	}
	
	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
	}

	@Override
	public void setItemStackNMS(org.bukkit.inventory.ItemStack stack) {
		ItemStack newItem = CraftItemStack.asNMSCopy(stack);
		newItem.count = 0;
		setItemStack(newItem);
	}


	@Override
	public HologramBase getParentHologram() {
		return parent;
	}

	@Override
	public void setParentHologram(HologramBase base) {
		this.parent = base;
	}

	@Override
	public void allowPickup(boolean pickup) {
		if (pickup) {
			super.pickupDelay = 0;
		} else {
			super.pickupDelay = Integer.MAX_VALUE;
		}
	}

}
