/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_15_R1;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.hologram.StandardItemLine;
import me.filoghost.holographicdisplays.common.nms.NMSCommons;
import me.filoghost.holographicdisplays.common.nms.entity.NMSItem;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityItem;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagString;
import net.minecraft.server.v1_15_R1.World;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class EntityNMSItem extends EntityItem implements NMSItem {

    private final StandardItemLine parentHologramLine;
    private final VersionNMSEntityHelper helper;
    private CraftEntity customBukkitEntity;

    public EntityNMSItem(World world, StandardItemLine parentHologramLine) {
        super(EntityTypes.ITEM, world);
        this.parentHologramLine = parentHologramLine;
        this.helper = new VersionNMSEntityHelper(this);
        
        super.pickupDelay = 32767; // Lock the item pickup delay, also prevents entities from picking up the item
    }

    @Override
    public void tick() {
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
    public void pickup(EntityHuman human) {
        if (human.locY() < super.locY() - 1.5 || human.locY() > super.locY() + 1.0) {
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
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        // Do not save NBT
        return nbttagcompound;
    }

    @Override
    public void f(NBTTagCompound nbttagcompound) {
        // Do not load NBT
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        // Do not load NBT
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
        // Prevent entity from dying
    }

    @Override
    public boolean isAlive() {
        // This override prevents items from being picked up by hoppers (should have no side effects)
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
        ItemStack newItem = CraftItemStack.asNMSCopy(stack);

        if (newItem == null || newItem == ItemStack.a) { // ItemStack.a is returned if the stack is not valid
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
        tagList.add(NBTTagString.a(NMSCommons.ANTI_STACK_LORE));
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
