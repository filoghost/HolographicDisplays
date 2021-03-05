/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.fcommons.reflection.ReflectMethod;
import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.core.Utils;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {
    
    private static final ReflectMethod<Void> SET_MARKER_METHOD = ReflectMethod.lookup(void.class, EntityArmorStand.class, "n", boolean.class);
    private static final ReflectField<Double> PASSENGER_PITCH_DELTA = ReflectField.lookup(double.class, Entity.class, "ar");
    private static final ReflectField<Double> PASSENGER_YAW_DELTA = ReflectField.lookup(double.class, Entity.class, "as");

    private final StandardHologramLine parentHologramLine;
    private final ProtocolPacketSettings protocolPacketSettings;
    private String customName;
    
    public EntityNMSArmorStand(World world, StandardHologramLine parentHologramLine, ProtocolPacketSettings protocolPacketSettings) {
        super(world);
        this.parentHologramLine = parentHologramLine;
        this.protocolPacketSettings = protocolPacketSettings;
        
        super.setInvisible(true);
        super.setSmall(true);
        super.setArms(false);
        super.setGravity(true);
        super.setBasePlate(true);
        try {
            SET_MARKER_METHOD.invoke(this, true);
        } catch (ReflectiveOperationException e) {
            DebugLogger.cannotSetArmorStandAsMarker(e);
            // It will still work, but the offset will be wrong.
        }
        super.noclip = true;
        super.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
        forceSetBoundingBox(new NullBoundingBox());
    }
    
    @Override
    public void t_() {
        // Disable normal ticking for this entity.
        
        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if (super.onGround) {
            super.onGround = false;
        }
    }
    
    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity.
        
        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if (super.onGround) {
            super.onGround = false;
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
    public void setCustomName(String customName) {
        // Locks the custom name.
    }
    
    @Override
    public void setCustomNameVisible(boolean visible) {
        // Locks the custom name.
    }

    @Override
    public boolean a(EntityHuman human, Vec3D vec3d) {
        // Prevent stand being equipped
        return true;
    }

    @Override
    public boolean d(int i, ItemStack item) {
        // Prevent stand being equipped
        return false;
    }

    @Override
    public void setEquipment(int i, ItemStack item) {
        // Prevent stand being equipped
    }
    
    @Override
    public void a(AxisAlignedBB boundingBox) {
        // Prevent bounding box from being changed
    }
    
    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
    }
    
    @Override
    public void makeSound(String sound, float f1, float f2) {
        // Remove sounds.
    }
    
    @Override
    public void setCustomNameNMS(String name) {
        this.customName = Utils.limitLength(name, 300);
        super.setCustomName(customName);
        super.setCustomNameVisible(customName != null && !customName.isEmpty());
    }
    
    @Override
    public String getCustomNameStringNMS() {
        return this.customName;
    }
    
    @Override
    public Object getCustomNameObjectNMS() {
        return super.getCustomName();
    }
    
    @Override
    public void die() {
        // Prevent being killed.
    }
    
    @Override
    public CraftEntity getBukkitEntity() {
        if (super.bukkitEntity == null) {
            super.bukkitEntity = new CraftNMSArmorStand(super.world.getServer(), this);
        }
        return super.bukkitEntity;
    }
    
    @Override
    public void killEntityNMS() {
        super.dead = true;
    }
    
    @Override
    public void setLocationNMS(double x, double y, double z) {
        super.setPosition(x, y, z);
        if (protocolPacketSettings.sendAccurateLocationPackets()) {
            broadcastLocationPacketNMS();
        }
    }
    
    private void broadcastLocationPacketNMS() {
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);

        for (Object humanEntity : super.world.players) {
            if (humanEntity instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer) humanEntity;

                double distanceSquared = Utils.square(nmsPlayer.locX - super.locX) + Utils.square(nmsPlayer.locZ - super.locZ);
                if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) {
                    nmsPlayer.playerConnection.sendPacket(teleportPacket);
                }
            }
        }
    }

    @Override
    public void setPassengerNMS(NMSEntity passenger) {
        Preconditions.checkArgument(passenger instanceof Entity);
        Entity passengerEntity = (Entity) passenger;
        Preconditions.checkArgument(passengerEntity.vehicle == null);
        Preconditions.checkState(super.passenger == null);

        try {
            PASSENGER_PITCH_DELTA.set(passengerEntity, 0.0);
            PASSENGER_YAW_DELTA.set(passengerEntity, 0.0);
        } catch (ReflectiveOperationException e) {
            DebugLogger.cannotSetPassengerPitchYawDelta(e);
        }

        passengerEntity.vehicle = this;
        super.passenger = passengerEntity;
    }

    @Override
    public boolean isDeadNMS() {
        return this.dead;
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
    
}
