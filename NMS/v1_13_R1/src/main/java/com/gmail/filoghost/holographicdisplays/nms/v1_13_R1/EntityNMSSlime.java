package com.gmail.filoghost.holographicdisplays.nms.v1_13_R1;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Non interactive version of the EntitySlime entity.
 */
public class EntityNMSSlime extends EntitySlime implements NMSSlime {

	private boolean lockTick;
	private HologramLine parentPiece;

	public EntityNMSSlime(World world, HologramLine parentPiece) {
		super(world);
		super.persistent = true;
		super.collides = false;
		setSize(0.0F, 0.0F); // Set the entity size
		setSize(1, false); // Set the slime size
		setInvisible(true);
		this.parentPiece = parentPiece;
	}

	/*
	 * Implement NMSEntityBase methods.
	 */

	@Override
	public HologramLine getHologramLine() {
		return parentPiece;
	}

	@Override
	public void setLockTick(boolean lock) {
		lockTick = lock;
	}

	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
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
	public int getIdNMS() {
		return super.getId();
	}

	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}

	/*
	 * Implement NMSCanMount methods.
	 */

	@Override
	public void setPassengerOfNMS(NMSEntityBase vehicleBase) {
		if (!(vehicleBase instanceof Entity)) {
			// It should never dismount.
			return;
		}

		Entity entity = (Entity) vehicleBase;
		try {
			if (super.getVehicle() != null) {
				super.getVehicle().passengers.remove(this);
			}

			ReflectionUtils.setPrivateField(Entity.class, this, "ax", entity);
			entity.passengers.clear();
			entity.passengers.add(this);
		} catch (Exception ex) {
			ConsoleLogger.error(ex);
		}
	}

	/*
	 * Handle vanilla Entity methods.
	 */

	// Do not save EntitySlime NBT data.
	@Override
	public void b(NBTTagCompound nbttagcompound) {
	}

	// Do not save Entity ID data.
	@Override
	public boolean c(NBTTagCompound nbttagcompound) {
		return false;
	}

	// Do not save Entity NBT data.
	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		return nbttagcompound;
	}

	// Do not load Entity NBT data.
	@Override
	public void f(NBTTagCompound nbttagcompound) {
	}

	// Do not load EntityArmourStand NBT data.
	@Override
	public void a(NBTTagCompound nbttagcompound) {
	}

	// Filter inactive ticks.
	@Override
	public void inactiveTick() {
		// Check inactive ticks.

		if (!lockTick) {
			super.inactiveTick();
		}
	}

	// Filter ticks.
	@Override
	public void tick() {
		// So it won't getCurrent removed automatically.
		ticksLived = 0;

		if (!lockTick) {
			super.tick();
		}
	}

	/*
	 * The field Entity.invulnerable is private.
	 * It's only used while saving NBTTags, but since the entity would be killed
	 * on chunk unload, we prefer to override isInvulnerable().
	 */
	@Override
	public boolean isInvulnerable(DamageSource source) {
		return true;
	}

	// Prevent entity collisions.
	@Override
	public boolean isCollidable() {
		return false;
	}

	// Prevent custom name changing.
	@Override
	public void setCustomName(IChatBaseComponent customName) {
	}

	// Prevent custom name visibility changing.
	@Override
	public void setCustomNameVisible(boolean visible) {
	}

	// Remove sounds.
	@Override
	public void a(SoundEffect soundeffect, float f, float f1) {
	}

	// Prevent being killed.
	@Override
	public void die() {
	}

	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			super.bukkitEntity = new CraftNMSSlime(super.world.getServer(), this);
		}
		return super.bukkitEntity;
	}

	/*
	 * Handle EntityLiving methods.
	 */

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
}
