package com.gmail.filoghost.holographicdisplays.nms.v1_13_R1;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.util.CraftChatMessage;

/**
 * Non interactive version of the EntityArmorStand entity.
 */
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

		// Send a teleport packet to update the position.
		PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);

		for (EntityHuman human : super.world.players) {
			if (human instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) human;

				double distanceSquared = Utils.square(player.locX - super.locX) + Utils.square(player.locZ - super.locZ);
				if (distanceSquared < 8192 && player.playerConnection != null) {
					player.playerConnection.sendPacket(teleportPacket);
				}
			}
		}
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
		// Return the real ID without filtering the caller method.
		return super.getId();
	}

	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return super.getBukkitEntity();
	}

	/*
	 * Implement NMSNameable methods.
	 */

	@Override
	public void setCustomNameNMS(String name) {
		if (name != null && name.length() > 300) {
			name = name.substring(0, 300);
		}
		super.setCustomName(CraftChatMessage.fromStringOrNull(name));
		super.setCustomNameVisible(name != null && !name.isEmpty());
	}

	@Override
	public String getCustomNameNMS() {
		return CraftChatMessage.fromComponent(super.getCustomName());
	}

	/*
	 * Handle vanilla Entity methods.
	 */

	// Return the non interactive bukkit entity.
	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			super.bukkitEntity = new CraftNMSArmorStand(super.world.getServer(), this);
		}
		return super.bukkitEntity;
	}

	// Do not save EntityArmourStand NBT data.
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

	// Prevent armour stand being equipped by players interaction.
	@Override
	public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand hand) {
		return EnumInteractionResult.PASS;
	}

	// Prevent armour stand being equipped via Entity method.
	@Override
	public boolean c(int i, ItemStack item) {
		return false;
	}

	// Prevent armour stand being equipped via EntityLiving method.
	@Override
	public void setSlot(EnumItemSlot slot, ItemStack item) {
	}

	// Prevent armour stand bounding box changing.
	@Override
	public void a(AxisAlignedBB boundingBox) {
	}

	// Inject fake id for movement packets.
	@Override
	public int getId() {
		StackTraceElement element = ReflectionUtils.getStackTraceElement(2);
		if (element != null && element.getFileName() != null && element.getFileName().equals("EntityTrackerEntry.java") && 158 < element.getLineNumber() && element.getLineNumber() < 168) {
			// Then this method is being called when creating a new movement packet, we return a fake ID!
			return -1;
		}

		return super.getId();
	}

	// Filter inactive ticks.
	@Override
	public void inactiveTick() {
		if (!lockTick) {
			super.inactiveTick();
		}
	}

	// Filter ticks.
	@Override
	public void tick() {
		if (!lockTick) {
			super.tick();
		}
	}

	// Remove sounds.
	@Override
	public void a(SoundEffect soundeffect, float f, float f1) {
	}

	// Prevent being killed.
	@Override
	public void die() {
	}
}