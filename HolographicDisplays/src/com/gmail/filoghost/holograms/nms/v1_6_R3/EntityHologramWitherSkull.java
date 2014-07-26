package com.gmail.filoghost.holograms.nms.v1_6_R3;

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;

import com.gmail.filoghost.holograms.nms.interfaces.BasicEntityNMS;
import com.gmail.filoghost.holograms.nms.interfaces.HologramWitherSkull;
import com.gmail.filoghost.holograms.object.HologramBase;
import com.gmail.filoghost.holograms.utils.GenericUtils;

import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.EntityWitherSkull;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.Packet34EntityTeleport;
import net.minecraft.server.v1_6_R3.World;
import net.minecraft.server.v1_6_R3.EntityPlayer;

public class EntityHologramWitherSkull extends EntityWitherSkull implements HologramWitherSkull {

	private boolean lockTick;
	private HologramBase parent;
	
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
	public void l_() {
		if (!lockTick) {
			super.l_();
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
	public void sendUpdatePacketNear() {
		
		// Send a packet to update the position.
		Packet34EntityTeleport teleportPacket = new Packet34EntityTeleport(this);
				
		for (Object obj : this.world.players) {
			if (obj instanceof EntityPlayer) {
				EntityPlayer nmsPlayer = (EntityPlayer) obj;
				
				double distanceSquared = GenericUtils.square(nmsPlayer.locX - this.locX) + GenericUtils.square(nmsPlayer.locZ - this.locZ);
				if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) {
					nmsPlayer.playerConnection.sendPacket(teleportPacket);
				}
			}
		}
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