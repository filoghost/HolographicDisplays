/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.nms.entity.NMSSlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityNMSSlime extends EntitySlime implements NMSSlime {

    private final StandardHologramLine parentHologramLine;
    private final VersionNMSEntityHelper helper;
    private CraftEntity customBukkitEntity;

    public EntityNMSSlime(World world, StandardHologramLine parentHologramLine) {
        super(EntityTypes.aD /* SLIME */, world);
        this.parentHologramLine = parentHologramLine;
        this.helper = new VersionNMSEntityHelper(this);

        super.collides = false;
        super.setPersistent();
        super.a(0.0F, 0.0F);
        super.setSize(1, false);
        super.setInvisible(true);
        forceSetBoundingBox(new NullBoundingBox());
    }

    @Override
    public void tick() {
        // Disable normal ticking for this entity

        // So it won't get removed
        super.R /* tickCount */ = 0;
    }

    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity

        // So it won't get removed
        super.R /* tickCount */ = 0;
    }

    @Override
    public void setPosition(double d0, double d1, double d2) {
        // Do not change it!
    }

    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        // Do not save NBT
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        // Do not save NBT
        return false;
    }

    @Override
    public boolean e(NBTTagCompound nbttagcompound) {
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
    public void killEntity() {
        // Prevent entity from dying
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (customBukkitEntity == null) {
            customBukkitEntity = new CraftNMSSlime(super.getWorld().getCraftServer(), this);
        }
        return customBukkitEntity;
    }

    @Override
    public boolean isDeadNMS() {
        return super.isRemoved();
    }

    @Override
    public void killEntityNMS() {
        super.setRemoved(RemovalReason.b /* DISCARDED */);
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
