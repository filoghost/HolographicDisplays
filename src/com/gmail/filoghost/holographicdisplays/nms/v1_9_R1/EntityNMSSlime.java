package com.gmail.filoghost.holographicdisplays.nms.v1_9_R1;

import net.minecraft.server.v1_9_R1.EntityDamageSource;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.AxisAlignedBB;
import net.minecraft.server.v1_9_R1.DamageSource;
import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntitySlime;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.PacketPlayOutMount;
import net.minecraft.server.v1_9_R1.SoundEffect;
import net.minecraft.server.v1_9_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class EntityNMSSlime extends EntitySlime implements NMSSlime {

	private boolean lockTick;
	private CraftTouchSlimeLine parentPiece;
	
	private int resendMountPacketTicks;
	
	public EntityNMSSlime(World world, CraftTouchSlimeLine parentPiece) {
		super(world);
		super.persistent = true;
		a(0.0F, 0.0F);
		setSize(1);
		setInvisible(true);
		this.parentPiece = parentPiece;
		forceSetBoundingBox(new NullBoundingBox());
	}
	
	@Override
	public void a(AxisAlignedBB boundingBox) {
		// Do not change it!
	}
	
	public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
		super.a(boundingBox);
	}
	
	@Override
	public void m() {
		
		// So it won't get removed.
		ticksLived = 0;
		
		if (resendMountPacketTicks++ > 20) {
			resendMountPacketTicks = 0;

			if (by() != null) {
				// Send a packet near to "remind" players that the slime is riding the armor stand (Spigot bug or client bug)
				PacketPlayOutMount mountPacket = new PacketPlayOutMount(by());
	
				for (Object obj : super.world.players) {
					if (obj instanceof EntityPlayer) {
						EntityPlayer nmsPlayer = (EntityPlayer) obj;
	
						double distanceSquared = Utils.square(nmsPlayer.locX - super.locX) + Utils.square(nmsPlayer.locZ - super.locZ);
						if (distanceSquared < 1024 && nmsPlayer.playerConnection != null) {
							nmsPlayer.playerConnection.sendPacket(mountPacket);
						}
					}
				}
			}
		}
		
		if (!lockTick) {
			super.m();
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
	public void f(NBTTagCompound nbttagcompound) {
		// Do not load NBT.
	}
	
	@Override
	public void a(NBTTagCompound nbttagcompound) {
		// Do not load NBT.
	}
	
	@Override
	public boolean damageEntity(DamageSource damageSource, float amount) {
		if (damageSource instanceof EntityDamageSource) {
			EntityDamageSource entityDamageSource = (EntityDamageSource) damageSource;
			if (entityDamageSource.getEntity() instanceof EntityPlayer) {
				Bukkit.getPluginManager().callEvent(new PlayerInteractEntityEvent(((EntityPlayer) entityDamageSource.getEntity()).getBukkitEntity(), getBukkitEntity())); // Bukkit takes care of the exceptions
			}
		}
		return false;
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
	public boolean isCollidable() {
		return false;
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
	public void a(SoundEffect soundeffect, float f, float f1) {
	    // Remove sounds.
	}
	
	@Override
	public void setLockTick(boolean lock) {
		lockTick = lock;
	}
	
	@Override
	public void die() {
		// Prevent being killed.
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			super.bukkitEntity = new CraftNMSSlime(super.world.getServer(), this);
	    }
		return super.bukkitEntity;
	}

	@Override
	public boolean isDeadNMS() {
		return super.dead;
	}
	
	@Override
	public void killEntityNMS() {
		super.dead = true;
	}
	
	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
	}
	
	@Override
	public int getIdNMS() {
		return super.getId();
	}
	
	@Override
	public CraftHologramLine getHologramLine() {
		return parentPiece;
	}

	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}
	
	@Override
	public void setPassengerOfNMS(NMSEntityBase vehicleBase) {
		if (vehicleBase == null || !(vehicleBase instanceof Entity)) {
			// It should never dismount
			return;
		}
		
		Entity entity = (Entity) vehicleBase;
		
		try {
			if (super.by() != null) {
	        	Entity oldVehicle = super.by();
	        	ReflectionUtils.setPrivateField(Entity.class, this, "as", null);
	        	oldVehicle.passengers.remove(this);
	        }

	        ReflectionUtils.setPrivateField(Entity.class, this, "as", entity);
	        entity.passengers.clear();
	        entity.passengers.add(this);

		} catch (Exception ex) {
			DebugHandler.handleDebugException(ex);
		}
	}
}
