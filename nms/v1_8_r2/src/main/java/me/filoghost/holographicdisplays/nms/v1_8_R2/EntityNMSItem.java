/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R2;

import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import me.filoghost.holographicdisplays.nms.interfaces.NMSCommons;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import net.minecraft.server.v1_8_R2.Blocks;
import net.minecraft.server.v1_8_R2.DamageSource;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EntityItem;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagList;
import net.minecraft.server.v1_8_R2.NBTTagString;
import net.minecraft.server.v1_8_R2.World;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class EntityNMSItem extends EntityItem implements NMSItem {
    
    private static final ReflectField<Double> RIDER_PITCH_DELTA = ReflectField.lookup(double.class, Entity.class, "ar");
    private static final ReflectField<Double> RIDER_YAW_DELTA = ReflectField.lookup(double.class, Entity.class, "as");
    
    private final ItemLine parentPiece;
    private final ItemPickupManager itemPickupManager;
    
    public EntityNMSItem(World world, ItemLine piece, ItemPickupManager itemPickupManager) {
        super(world);
        super.pickupDelay = Integer.MAX_VALUE;
        this.parentPiece = piece;
        this.itemPickupManager = itemPickupManager;
    }
    
    @Override
    public void t_() {
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
    public void d(EntityHuman human) {
        if (human.locY < this.locY - 1.5 || human.locY > this.locY + 1.0) {
            // Too low or too high, it's a bit weird.
            return;
        }
        
        if (parentPiece.getPickupHandler() != null && human instanceof EntityPlayer) {
            itemPickupManager.handleItemLinePickup((Player) human.getBukkitEntity(), parentPiece);
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
    public boolean isInvulnerable(DamageSource source) {
        /*
         * The field Entity.invulnerable is private.
         * It's only used while saving NBTTags, but since the entity would be killed
         * on chunk unload, we prefer to override isInvulnerable().
         */
        return true;
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
        tagList.add(new NBTTagString(NMSCommons.ANTISTACK_LORE)); // Antistack lore
        display.set("Lore", tagList);
        
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
    public org.bukkit.entity.Entity getBukkitEntityNMS() {
        return getBukkitEntity();
    }
    
    @Override
    public void setPassengerOfNMS(NMSEntityBase vehicleBase) {
        if (!(vehicleBase instanceof Entity)) {
            // It should never dismount
            return;
        }
        
        Entity entity = (Entity) vehicleBase;
        
        try {
            RIDER_PITCH_DELTA.set(this, 0.0);
            RIDER_YAW_DELTA.set(this, 0.0);
        } catch (Throwable t) {
            DebugLogger.cannotSetRiderPitchYaw(t);
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
