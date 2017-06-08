package com.gmail.filoghost.holographicdisplays.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityItem;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import net.minecraft.server.v1_12_R1.World;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.listener.MainListener;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;

public class EntityNMSItem extends EntityItem implements NMSItem {
	
	private boolean lockTick;
	private CraftItemLine parentPiece;
	
	public EntityNMSItem(World world, CraftItemLine piece) {
		super(world);
		super.pickupDelay = 32767; // Lock the item pickup delay, also prevents entities from picking up the item
		this.parentPiece = piece;
	}
	
	@Override
	public void B_() {
		
		// So it won't get removed.
		ticksLived = 0;
		
		if (!lockTick) {
			super.B_();
		}
	}
	
	// Method called when a player is near.
	@Override
	public void d(EntityHuman human) {
		
		if (human.locY < super.locY - 1.5 || human.locY > super.locY + 1.0) {
			// Too low or too high, it's a bit weird.
			return;
		}
		
		if (parentPiece.getPickupHandler() != null && human instanceof EntityPlayer) {
			MainListener.handleItemLinePickup((Player) human.getBukkitEntity(), parentPiece.getPickupHandler(), parentPiece.getParent());
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
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
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
	public void inactiveTick() {
		// Check inactive ticks.
		
		if (!lockTick) {
			super.inactiveTick();
		}
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
			super.bukkitEntity = new CraftNMSItem(super.world.getServer(), this);
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
	public void setItemStackNMS(org.bukkit.inventory.ItemStack stack) {
		ItemStack newItem = CraftItemStack.asNMSCopy(stack);
		
		if (newItem == null) {
			newItem = new ItemStack(Blocks.BEDROCK);
		}

		if (newItem.getTag() == null) {
			newItem.setTag(new NBTTagCompound());
		}
		NBTTagCompound display = newItem.getTag().getCompound("display"); // Returns a new NBTTagCompound if not existing
		if (!newItem.getTag().hasKey("display")) {
			newItem.getTag().set("display", display);
		}

		NBTTagList tagList = new NBTTagList();
		tagList.add(new NBTTagString(ItemUtils.ANTISTACK_LORE)); // Antistack lore
		display.set("Lore", tagList);

		newItem.setCount(1);
		
		setItemStack(newItem);
	}
	
	@Override
	public int getIdNMS() {
		return super.getId();
	}
	
	@Override
	public CraftItemLine getHologramLine() {
		return parentPiece;
	}
	
	@Override
	public void allowPickup(boolean pickup) {
		if (pickup) {
			super.pickupDelay = 0;
		} else {
			super.pickupDelay = 32767;
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
			if (super.bJ() != null) {
	        	Entity oldVehicle = super.bJ();
	        	ReflectionUtils.setPrivateField(Entity.class, this, "au", null);
	        	oldVehicle.passengers.remove(this);
	        }

	        ReflectionUtils.setPrivateField(Entity.class, this, "au", entity);
	        entity.passengers.clear();
	        entity.passengers.add(this);

		} catch (Exception ex) {
			DebugHandler.handleDebugException(ex);
		}
	}

	@Override
	public Object getRawItemStack() {
		return super.getItemStack();
	}
}
