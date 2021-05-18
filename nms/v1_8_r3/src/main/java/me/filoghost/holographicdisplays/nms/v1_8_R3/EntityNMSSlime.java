/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityDamageSource;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
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
        super.noclip = true;
        super.a(0.0F, 0.0F);
        super.setSize(1);
        super.setInvisible(true);
        forceSetBoundingBox(new NullBoundingBox());
    }
    
    @Override
    public void t_() {
        // Disable normal ticking for this entity
        
        // So it won't get removed
        ticksLived = 0;

        // The slime dies without a vehicle
        if (super.vehicle == null) {
            killEntityNMS();
        }
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
    public void e(NBTTagCompound nbttagcompound) {
        // Do not save NBT
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
    public void setCustomName(String customName) {
        // Prevents changes to custom name
    }
    
    @Override
    public void setCustomNameVisible(boolean visible) {
        // Prevents changes to custom name visibility
    }
    
    @Override
    public void makeSound(String sound, float volume, float pitch) {
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
