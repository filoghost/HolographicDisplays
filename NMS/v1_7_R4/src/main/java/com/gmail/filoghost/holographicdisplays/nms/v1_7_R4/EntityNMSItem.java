package com.gmail.filoghost.holographicdisplays.nms.v1_7_R4;

import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.bukkit.ItemUtils;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class EntityNMSItem extends EntityItem implements NMSItem {

	private boolean lockTick;
	private ItemLine parentPiece;
	private ItemPickupManager itemPickupManager;

	public EntityNMSItem(World world, ItemLine piece, ItemPickupManager itemPickupManager) {
		super(world);
		super.pickupDelay = Integer.MAX_VALUE;
		this.parentPiece = piece;
		this.itemPickupManager = itemPickupManager;
	}

	@Override
	public void h() {

		// So it won't getCurrent removed.
		ticksLived = 0;
		age = 0;
		pickupDelay = Integer.MAX_VALUE;

		if (!lockTick) {
			super.h();
		}
	}

	@Override
	public ItemStack getItemStack() {
		// Dirty method to check if the icon is being picked up
		StackTraceElement element = ReflectionUtils.getStackTraceElement(2);
		if (element != null && element.getFileName() != null && element.getClassName().contains("EntityInsentient")) {
			return null; // Try to pickup this, dear entity ignoring the pickupDelay!
		}

		return super.getItemStack();
	}

	// Method called when a player is near.
	@Override
	public void b_(EntityHuman human) {

		if (parentPiece.getPickupHandler() != null && human instanceof EntityPlayer) {
			itemPickupManager.handleItemLinePickup((Player) human.getBukkitEntity(), parentPiece.getPickupHandler(), parentPiece.getParent());
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
			this.bukkitEntity = new CraftNMSItem(this.world.getServer(), this);
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

		if (newItem == null) {
			newItem = new ItemStack(Blocks.BEDROCK);
		}

		if (newItem.tag == null) {
			newItem.tag = new NBTTagCompound();
		}
		NBTTagCompound display = newItem.tag.getCompound("display");

		if (!newItem.tag.hasKey("display")) {
			newItem.tag.set("display", display);
		}

		NBTTagList tagList = new NBTTagList();
		tagList.add(new NBTTagString(ItemUtils.ANTISTACK_LORE)); // Antistack lore

		display.set("Lore", tagList);
		newItem.count = 0;
		setItemStack(newItem);
	}

	@Override
	public int getIdNMS() {
		return this.getId();
	}

	@Override
	public ItemLine getHologramLine() {
		return parentPiece;
	}

	@Override
	public void allowPickup(boolean pickup) {
		if (pickup) {
			super.pickupDelay = 0;
		} else {
			super.pickupDelay = Integer.MAX_VALUE;
		}
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
			ReflectionUtils.setPrivateField(Entity.class, this, "g", 0.0);
			ReflectionUtils.setPrivateField(Entity.class, this, "h", 0.0);
		} catch (Exception ex) {
			ConsoleLogger.error(ex);
		}

		if (this.vehicle != null) {
			this.vehicle.passenger = null;
		}

		this.vehicle = entity;
		entity.passenger = this;
	}

	@Override
	public Object getRawItemStack() {
		return super.getItemStack();
	}
}
