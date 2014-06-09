package com.gmail.filoghost.holograms.nms.v1_7_R2;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;

import com.gmail.filoghost.holograms.nms.interfaces.BasicEntityNMS;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.object.CraftHologram;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityWitherSkull;
import net.minecraft.server.v1_7_R2.NBTTagCompound;
import net.minecraft.server.v1_7_R2.World;

public class EntityHologramWitherSkull extends EntityWitherSkull implements HologramWitherSkull {

	private boolean lockTick;
	private CraftHologram parent;
	
	public EntityHologramWitherSkull(World world) {
		super(world);
		super.motX = 0.0;
		super.motY = 0.0;
		super.motZ = 0.0;
		super.dirX = 0.0;
		super.dirY = 0.0;
		super.dirZ = 0.0;
		super.boundingBox.a = 0.0;
		super.boundingBox.b = 0.0;
		super.boundingBox.c = 0.0;
		super.boundingBox.d = 0.0;
		super.boundingBox.e = 0.0;
		super.boundingBox.f = 0.0;
		a(0.0F, 0.0F);
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
	public void h() {
		if (!lockTick) {
			super.h();
		}
	}
	
	@Override
	public void makeSound(String sound, float f1, float f2) {
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
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new CraftHologramWitherSkull(this.world.getServer(), this);
	    }
		return this.bukkitEntity;
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
	public boolean isDeadNMS() {
		return this.dead;
	}
	
	@Override
	public void setPassengerNMS(BasicEntityNMS passenger) {
		if (passenger instanceof Entity) {
			((Entity) passenger).setPassengerOf(this);
		}
	}
	
	@Override
	public void setPassengerNMS(org.bukkit.entity.Entity bukkitEntity) {
		((CraftEntity) bukkitEntity).getHandle().setPassengerOf(this);
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