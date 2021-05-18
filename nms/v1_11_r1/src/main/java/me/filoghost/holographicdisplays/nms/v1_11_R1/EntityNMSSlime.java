/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_11_R1;

import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import net.minecraft.server.v1_11_R1.AxisAlignedBB;
import net.minecraft.server.v1_11_R1.DamageSource;
import net.minecraft.server.v1_11_R1.EntityDamageSource;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.EntitySlime;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.SoundEffect;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityNMSSlime extends EntitySlime implements NMSSlime {
    
    private final StandardHologramLine parentHologramLine;
    private final VersionNMSEntityHelper helper;
    
    public EntityNMSSlime(World world, StandardHologramLine parentHologramLine) {
        super(world);
        this.parentHologramLine = parentHologramLine;
        this.helper = new VersionNMSEntityHelper(this);
        
        super.persistent = true;
        super.collides = false;
        super.a(0.0F, 0.0F);
        super.setSize(1, false);
        super.setInvisible(true);
        forceSetBoundingBox(new NullBoundingBox());
    }
    
    @Override
    public void A_() {
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
    
    @Override
    public void a(AxisAlignedBB boundingBox) {
        // Prevent changes to bounding box
    }
    
    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
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
    public NBTTagCompound e(NBTTagCompound nbttagcompound) {
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
    public boolean damageEntity(DamageSource damageSource, float amount) {
        if (damageSource instanceof EntityDamageSource) {
            EntityDamageSource entityDamageSource = (EntityDamageSource) damageSource;
            if (entityDamageSource.getEntity() instanceof EntityPlayer) {
                Player player = ((EntityPlayer) entityDamageSource.getEntity()).getBukkitEntity();
                PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(player, getBukkitEntity());
                Bukkit.getPluginManager().callEvent(event); // Bukkit takes care of the exceptions
            }
        }
        return false;
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
        // Prevents changes to custom name
    }
    
    @Override
    public void setCustomNameVisible(boolean visible) {
        // Prevents changes to custom name visibility
    }
    
    @Override
    public void a(SoundEffect soundeffect, float f, float f1) {
        // Remove sounds
    }
    
    @Override
    public void die() {
        // Prevent entity from dying
    }
    
    @Override
    public CraftEntity getBukkitEntity() {
        if (super.bukkitEntity == null) {
            super.bukkitEntity = new CraftNMSSlime(super.world.getServer(), this);
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
    
}
