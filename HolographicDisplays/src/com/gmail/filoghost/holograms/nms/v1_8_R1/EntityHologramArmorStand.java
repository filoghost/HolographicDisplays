package com.gmail.filoghost.holograms.nms.v1_8_R1;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityArmorStand;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.Vec3D;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;

import com.gmail.filoghost.holograms.nms.interfaces.BasicEntityNMS;
import com.gmail.filoghost.holograms.nms.interfaces.HologramArmorStand;
import com.gmail.filoghost.holograms.object.HologramBase;
import com.gmail.filoghost.holograms.utils.ReflectionUtils;

public class EntityHologramArmorStand extends EntityArmorStand implements HologramArmorStand {

	private boolean lockTick;
	private HologramBase parent;
	
	public EntityHologramArmorStand(World world) {
		super(world);
		setInvisible(true);
		setSmall(true);
		setArms(false);
		setGravity(true);
		setBasePlate(true);
		try {
			ReflectionUtils.setPrivateField(EntityArmorStand.class, this, "bg", Integer.MAX_VALUE);
		} catch (Exception e) {
			// There's still the overridden method.
		}
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
	public boolean isInvulnerable(DamageSource source) {
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
	public boolean a(EntityHuman human, Vec3D vec3d) {
		// Prevent stand being equipped
		return true;
	}

	@Override
	public boolean d(int i, ItemStack item) {
		// Prevent stand being equipped
		return false;
	}

	@Override
	public void setEquipment(int i, ItemStack item) {
		// Prevent stand being equipped
	}


	@Override
	public void s_() {
		if (!lockTick) {
			super.s_();
		}
	}
	
	@Override
	public void makeSound(String sound, float f1, float f2) {
	    // Remove sounds.
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
	public String getCustomNameNMS() {
		return super.getCustomName();
	}
	
	
	public void callSuperTick() {
		super.h();
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
			this.bukkitEntity = new CraftHologramArmorStand(this.world.getServer(), this);
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
	public HologramBase getParentHologram() {
		return parent;
	}

	@Override
	public void setParentHologram(HologramBase base) {
		this.parent = base;
	}
}