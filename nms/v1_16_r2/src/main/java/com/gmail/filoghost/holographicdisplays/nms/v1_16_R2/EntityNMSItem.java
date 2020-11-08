/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.nms.v1_16_R2;

import java.util.logging.Level;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;
import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectField;

import net.minecraft.server.v1_16_R2.Blocks;
import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityItem;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.ItemStack;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.NBTTagList;
import net.minecraft.server.v1_16_R2.NBTTagString;
import net.minecraft.server.v1_16_R2.World;

public class EntityNMSItem extends EntityItem implements NMSItem {

    private static final ReflectField<Entity> VEHICLE_FIELD = new ReflectField<>(Entity.class, "vehicle");

    private ItemLine parentPiece;
    private ItemPickupManager itemPickupManager;
    private CraftEntity customBukkitEntity;

    public EntityNMSItem(World world, ItemLine piece, ItemPickupManager itemPickupManager) {
        super(EntityTypes.ITEM, world);
        super.pickupDelay = 32767; // Lock the item pickup delay, also prevents entities from picking up the item
        this.parentPiece = piece;
        this.itemPickupManager = itemPickupManager;
    }

    @Override
    public void tick() {
    	// Disable normal ticking for this entity.

        // So it won't get removed.
        ticksLived = 0;
    }
    
    @Override
	public void inactiveTick() {
		// Disable normal ticking for this entity.
    	
    	// So it won't get removed.
    	ticksLived = 0;
	}

    // Method called when a player is near.
    @Override
    public void pickup(EntityHuman human) {

        if (human.locY() < super.locY() - 1.5 || human.locY() > super.locY() + 1.0) {
            // Too low or too high, it's a bit weird./
            return;
        }

        if (parentPiece.getPickupHandler() != null && human instanceof EntityPlayer) {
            itemPickupManager.handleItemLinePickup((Player) human.getBukkitEntity(), parentPiece.getPickupHandler(), parentPiece.getParent());
            // It is never added to the inventory.
        }
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
    }

    @Override
    public boolean a_(NBTTagCompound nbttagcompound) {
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
    public void load(NBTTagCompound nbttagcompound) {
        // Do not load NBT.
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
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
    public void die() {
        // Prevent being killed.
    }

    @Override
    public boolean isAlive() {
        // This override prevents items from being picked up by hoppers.
        // Should have no side effects.
        return false;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (customBukkitEntity == null) {
            customBukkitEntity = new CraftNMSItem(super.world.getServer(), this);
        }
        return customBukkitEntity;
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
        ItemStack newItem = CraftItemStack.asNMSCopy(stack); // ItemStack.a is returned if the stack is null, invalid or the material is not an Item

        if (newItem == null || newItem == ItemStack.b) {
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
        tagList.add(NBTTagString.a(ItemUtils.ANTISTACK_LORE)); // Antistack lore
        display.set("Lore", tagList);

        setItemStack(newItem);
    }

    @Override
    public int getIdNMS() {
        return super.getId();
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
            if (super.getVehicle() != null) {
                Entity oldVehicle = super.getVehicle();
                VEHICLE_FIELD.set(this, null);
                oldVehicle.passengers.remove(this);
            }

            VEHICLE_FIELD.set(this, entity);
            entity.passengers.clear();
            entity.passengers.add(this);

        } catch (Throwable t) {
            ConsoleLogger.logDebug(Level.SEVERE, "Couldn't set passenger", t);
        }
    }

    @Override
    public Object getRawItemStack() {
        return super.getItemStack();
    }
}
