package com.gmail.filoghost.holographicdisplays.nms.v1_11_R1;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {

	private boolean lockTick;
	private HologramLine parentPiece;

	public EntityNMSArmorStand(World world, HologramLine parentPiece) {
		super(world);
		super.setInvisible(true);
		super.setSmall(true);
		super.setArms(false);
		super.setNoGravity(true);
		super.setBasePlate(true);
		super.setMarker(true);
		super.collides = false;
		this.parentPiece = parentPiece;
		setSize(0.0F, 0.0F);
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
	public NBTTagCompound e(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return nbttagcompound;
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
	public void inactiveTick() {
		// Check inactive ticks.

		if (!lockTick) {
			super.inactiveTick();
		}
	}

	@Override
	public void setCustomNameVisible(boolean visible) {
		// Locks the custom name.
	}

	@Override
	public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
		// Prevent stand being equipped
		return EnumInteractionResult.PASS;
	}

	@Override
	public boolean c(int i, ItemStack item) {
		// Prevent stand being equipped
		return false;
	}

	@Override
	public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
		// Prevent stand being equipped
	}

	@Override
	public void a(AxisAlignedBB boundingBox) {
		// Do not change it!
	}

	@Override
	public int getId() {
		StackTraceElement element = ReflectionUtils.getStackTraceElement(2);
		if (element != null && element.getFileName() != null && element.getFileName().equals("EntityTrackerEntry.java") && 158 < element.getLineNumber() && element.getLineNumber() < 168) {
			// Then this method is being called when creating a new movement packet, we return a fake ID!
			return -1;
		}

		return super.getId();
	}

	@Override
	public void A_() {
		if (!lockTick) {
			super.A_();
		}
	}

	@Override
	public void a(SoundEffect soundeffect, float f, float f1) {
		// Remove sounds.
	}

	@Override
	public void setCustomNameNMS(String name) {
		if (name != null && name.length() > 300) {
			name = name.substring(0, 300);
		}
		super.setCustomName(name);
		super.setCustomNameVisible(name != null && !name.isEmpty());
	}

	@Override
	public String getCustomNameNMS() {
		return super.getCustomName();
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
			super.bukkitEntity = new CraftNMSArmorStand(super.world.getServer(), this);
		}
		return super.bukkitEntity;
	}

	@Override
	public void killEntityNMS() {
		super.dead = true;
	}

	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);

		// Send a packet near to update the position.
		PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);

		for (Object obj : super.world.players) {
			if (obj instanceof EntityPlayer) {
				EntityPlayer nmsPlayer = (EntityPlayer) obj;

				double distanceSquared = Utils.square(nmsPlayer.locX - super.locX) + Utils.square(nmsPlayer.locZ - super.locZ);
				if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) {
					nmsPlayer.playerConnection.sendPacket(teleportPacket);
				}
			}
		}
	}

	@Override
	public boolean isDeadNMS() {
		return super.dead;
	}

	@Override
	public int getIdNMS() {
		return super.getId(); // Return the real ID without checking the stack trace.
	}

	@Override
	public HologramLine getHologramLine() {
		return parentPiece;
	}

	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}
}