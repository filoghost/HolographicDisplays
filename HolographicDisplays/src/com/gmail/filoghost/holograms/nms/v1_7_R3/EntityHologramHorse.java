package com.gmail.filoghost.holograms.nms.v1_7_R3;

import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;

import com.gmail.filoghost.holograms.nms.interfaces.HologramHorse;
import com.gmail.filoghost.holograms.object.HologramBase;

import net.minecraft.server.v1_7_R3.EntityHorse;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.World;

public class EntityHologramHorse extends EntityHorse implements HologramHorse {

	private boolean lockTick;
	private HologramBase parent;
	
	public EntityHologramHorse(World world) {
		super(world);
		super.ageLocked = true;
		super.persistent = true;
		super.boundingBox.a = 0.0;
		super.boundingBox.b = 0.0;
		super.boundingBox.c = 0.0;
		super.boundingBox.d = 0.0;
		super.boundingBox.e = 0.0;
		super.boundingBox.f = 0.0;
		a(0.0F, 0.0F);
		setAge(-1700000); // This is a magic value. No one will see the real horse.
	}
	
	@Override
	public void h() {
		// Checks every 20 ticks.
		if (ticksLived % 20 == 0) {
			// The horse dies without a vehicle.
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
	public void setCustomName(String customName) {
		// Locks the custom name.
	}
	
	@Override
	public void setCustomNameVisible(boolean visible) {
		// Locks the custom name.
	}
	
	@Override
	public void makeSound(String sound, float volume, float pitch) {
	    // Remove sounds.
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
	public void forceSetCustomName(String name) {
		if (name != null && name.length() > 300) {
			name = name.substring(0, 300);
		}
		super.setCustomName(name);
		super.setCustomNameVisible(name != null);
	}

	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new CraftHologramHorse(this.world.getServer(), this);
	    }
		return this.bukkitEntity;
	}

	@Override
	public boolean isDeadNMS() {
		return this.dead;
	}
	
	@Override
	public String getCustomNameNMS() {
		return super.getCustomName();
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
	public HologramBase getParentHologram() {
		return parent;
	}

	@Override
	public void setParentHologram(HologramBase base) {
		this.parent = base;
	}
}
