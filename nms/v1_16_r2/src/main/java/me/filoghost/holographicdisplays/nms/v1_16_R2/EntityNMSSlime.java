/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R2;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.nms.entity.NMSSlime;
import net.minecraft.server.v1_16_R2.AxisAlignedBB;
import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.EntityDamageSource;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.EntitySlime;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.SoundEffect;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityNMSSlime extends EntitySlime implements NMSSlime {
    
    private final StandardHologramLine parentHologramLine;
    private final VersionNMSEntityHelper helper;
    private CraftEntity customBukkitEntity;
    
    public EntityNMSSlime(World world, StandardHologramLine parentHologramLine) {
        super(EntityTypes.SLIME, world);
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
    
    @Override
    public void a(AxisAlignedBB boundingBox) {
        // Prevent changes to bounding box
    }
    
    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
    }
    
    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        // Do not save NBT
    }
    
    @Override
    public boolean a_(NBTTagCompound nbttagcompound) {
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
    public void load(NBTTagCompound nbttagcompound) {
        // Do not load NBT
    }
    
    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
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
    public void setCustomName(IChatBaseComponent ichatbasecomponent) {
        // Prevents changes to custom name
    }
    
    @Override
    public void setCustomNameVisible(boolean visible) {
        // Prevents changes to custom name visibility
    }
    
    @Override
    public void playSound(SoundEffect soundeffect, float f, float f1) {
        // Remove sounds
    }
    
    @Override
    public void die() {
        // Prevent entity from dying
    }
    
    @Override
    public CraftEntity getBukkitEntity() {
        if (customBukkitEntity == null) {
            customBukkitEntity = new CraftNMSSlime(super.world.getServer(), this);
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
