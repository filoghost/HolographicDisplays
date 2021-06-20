/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R2;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.common.nms.NMSCommons;
import me.filoghost.holographicdisplays.common.nms.entity.NMSItem;
import net.minecraft.server.v1_8_R2.Blocks;
import net.minecraft.server.v1_8_R2.DamageSource;
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
    
    private final StandardItemLine parentHologramLine;
    private final VersionNMSEntityHelper helper;

    public EntityNMSItem(World world, StandardItemLine parentHologramLine) {
        super(world);
        this.parentHologramLine = parentHologramLine;
        this.helper = new VersionNMSEntityHelper(this);
        
        super.pickupDelay = Integer.MAX_VALUE;
    }
    
    @Override
    public void t_() {
        // Disable normal ticking for this entity
        
        // So it won't get removed
        ticksLived = 0;
    }
    
    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity
        
        // So it won't get removed
        ticksLived = 0;
    }
    
    // Method called when a player is near
    @Override
    public void d(EntityHuman human) {
        if (human.locY < this.locY - 1.5 || human.locY > this.locY + 1.0) {
            // Too low or too high, it's a bit weird
            return;
        }
        
        if (human instanceof EntityPlayer) {
            parentHologramLine.onPickup(((EntityPlayer) human).getBukkitEntity());
            // It is never added to the inventory
        }
    }
    
    @Override
    public void b(NBTTagCompound nbttagcompound) {
        // Do not save NBT
    }
    
    @Override
    public boolean c(NBTTagCompound nbttagcompound) {
        // Do not save NBT
        return false;
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        // Do not save NBT
        return false;
    }
    
    @Override
    public void e(NBTTagCompound nbttagcompound) {
        // Do not save NBT
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
        // Prevent entity from dying
    }
    
    @Override
    public boolean isAlive() {
        // This override prevents items from being picked up by hoppers (should have no side effects)
        return false;
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
        tagList.add(new NBTTagString(NMSCommons.ANTI_STACK_LORE));
        display.set("Lore", tagList);
        
        super.setItemStack(newItem);
    }
    
    @Override
    public int getIdNMS() {
        return super.getId();
    }
    
    @Override
    public StandardHologramLine getHologramLine() {
        return parentHologramLine;
    }

    @Override
    public org.bukkit.entity.Entity getBukkitEntityNMS() {
        return getBukkitEntity();
    }

    @Override
    public boolean isTrackedBy(Player bukkitPlayer) {
        return helper.isTrackedBy(bukkitPlayer);
    }
    
    @Override
    public Object getRawItemStack() {
        return super.getItemStack();
    }
    
}
