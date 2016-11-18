package com.gmail.filoghost.holographicdisplays.nms.v1_9_R2;

import net.minecraft.server.v1_9_R2.Entity;
import net.minecraft.server.v1_9_R2.Blocks;
import net.minecraft.server.v1_9_R2.DamageSource;
import net.minecraft.server.v1_9_R2.EntityHuman;
import net.minecraft.server.v1_9_R2.EntityItem;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.ItemStack;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;
import net.minecraft.server.v1_9_R2.NBTTagString;
import net.minecraft.server.v1_9_R2.PacketPlayOutMount;
import net.minecraft.server.v1_9_R2.World;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.listener.MainListener;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.util.DebugHandler;
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class EntityNMSItem extends EntityItem implements NMSItem {
	
	private boolean lockTick;
	private CraftItemLine parentPiece;
	
	private int resendMountPacketTicks;
	
	public EntityNMSItem(World world, CraftItemLine piece) {
		super(world);
		super.pickupDelay = Integer.MAX_VALUE;
		this.parentPiece = piece;
	}
	
	@Override
	public void m() {
		
		// So it won't get removed.
		ticksLived = 0;
		
		if (resendMountPacketTicks++ > 20) {
			resendMountPacketTicks = 0;
			
			if (bz() != null) {
				// Send a packet near to "remind" players that the item is riding the armor stand (Spigot bug or client bug)
				PacketPlayOutMount mountPacket = new PacketPlayOutMount(bz());
	
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
		NBTTagCompound display = newItem.getTag().getCompound("display");
		
		if (!newItem.getTag().hasKey("display")) {
		newItem.getTag().set("display", display);
		}
		
		NBTTagList tagList = new NBTTagList();
		tagList.add(new NBTTagString(ItemUtils.ANTISTACK_LORE)); // Antistack lore
		
		display.set("Lore", tagList);
		newItem.count = 0;
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
			if (super.bz() != null) {
	        	Entity oldVehicle = super.bz();
	        	ReflectionUtils.setPrivateField(Entity.class, this, "at", null);
	        	oldVehicle.passengers.remove(this);
	        }

	        ReflectionUtils.setPrivateField(Entity.class, this, "at", entity);
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
