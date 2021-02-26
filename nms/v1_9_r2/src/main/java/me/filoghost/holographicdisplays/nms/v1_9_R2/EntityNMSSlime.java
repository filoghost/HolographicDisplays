/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_9_R2;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.core.nms.entity.NMSSlime;
import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.core.Utils;
import me.filoghost.fcommons.reflection.ReflectField;
import net.minecraft.server.v1_9_R2.AxisAlignedBB;
import net.minecraft.server.v1_9_R2.DamageSource;
import net.minecraft.server.v1_9_R2.Entity;
import net.minecraft.server.v1_9_R2.EntityDamageSource;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.EntitySlime;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.PacketPlayOutMount;
import net.minecraft.server.v1_9_R2.SoundEffect;
import net.minecraft.server.v1_9_R2.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityNMSSlime extends EntitySlime implements NMSSlime {
    
    private static final ReflectField<Entity> VEHICLE_FIELD = ReflectField.lookup(Entity.class, Entity.class, "at");

    private final HologramLine parentPiece;
    
    private int resendMountPacketTicks;
    
    public EntityNMSSlime(World world, HologramLine parentPiece) {
        super(world);
        super.persistent = true;
        a(0.0F, 0.0F);
        setSize(1);
        setInvisible(true);
        this.parentPiece = parentPiece;
        forceSetBoundingBox(new NullBoundingBox());
    }
    
    @Override
    public void m() {
        // Disable normal ticking for this entity.
        
        // So it won't get removed.
        ticksLived = 0;
        
        if (resendMountPacketTicks++ > 20) {
            resendMountPacketTicks = 0;

            Entity vehicle = bz();
            if (vehicle != null) {
                // Send a packet near to "remind" players that the slime is riding the armor stand (Spigot bug or client bug)
                PacketPlayOutMount mountPacket = new PacketPlayOutMount(vehicle);
    
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
    }
    
    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity.
        
        // So it won't get removed.
        ticksLived = 0;
    }
    
    @Override
    public void a(AxisAlignedBB boundingBox) {
        // Do not change it!
    }
    
    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
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
    public boolean damageEntity(DamageSource damageSource, float amount) {
        if (damageSource instanceof EntityDamageSource) {
            EntityDamageSource entityDamageSource = (EntityDamageSource) damageSource;
            if (entityDamageSource.getEntity() instanceof EntityPlayer) {
                Bukkit.getPluginManager().callEvent(new PlayerInteractEntityEvent(((EntityPlayer) entityDamageSource.getEntity()).getBukkitEntity(), getBukkitEntity())); // Bukkit takes care of the exceptions
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
        // Locks the custom name.
    }
    
    @Override
    public void setCustomNameVisible(boolean visible) {
        // Locks the custom name.
    }
    
    @Override
    public void a(SoundEffect soundeffect, float f, float f1) {
        // Remove sounds.
    }
    
    @Override
    public void die() {
        // Prevent being killed.
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
    public HologramLine getHologramLine() {
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
            Entity oldVehicle = super.bz();
            if (oldVehicle != null) {
                VEHICLE_FIELD.set(this, null);
                oldVehicle.passengers.remove(this);
            }

            VEHICLE_FIELD.set(this, entity);
            entity.passengers.clear();
            entity.passengers.add(this);

        } catch (Throwable t) {
            DebugLogger.cannotSetPassenger(t);
        }
    }
}
