package com.gmail.filoghost.holograms.nms.v1_7_R2;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.util.CraftMagicNumbers;

import com.gmail.filoghost.holograms.nms.interfaces.BasicEntityNMS;
import com.gmail.filoghost.holograms.nms.interfaces.CustomItem;
import com.gmail.filoghost.holograms.object.CraftHologram;
import net.minecraft.server.v1_7_R2.EntityItem;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.NBTTagCompound;
import net.minecraft.server.v1_7_R2.World;

public class EntityCustomItem extends EntityItem implements CustomItem, BasicEntityNMS {
	
	private boolean lockTick;
	private CraftHologram parent;
	
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
		setItemStack( new ItemStack(CraftMagicNumbers.getItem(stack.getType()), 1, stack.getDurability()) );
	}

	@Override
	public CraftHologram getParentHologram() {
		return parent;
	}

	@Override
	public void setParentHologram(CraftHologram hologram) {
		this.parent = hologram;
	}

}
