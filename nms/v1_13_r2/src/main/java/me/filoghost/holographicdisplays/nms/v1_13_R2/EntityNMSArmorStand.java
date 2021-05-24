/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_13_R2;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import net.minecraft.server.v1_13_R2.AxisAlignedBB;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumInteractionResult;
import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.Objects;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {

    private static final ReflectField<Entity> VEHICLE_FIELD = ReflectField.lookup(Entity.class, Entity.class, "vehicle");

    private final StandardHologramLine parentHologramLine;
    private final ProtocolPacketSettings protocolPacketSettings;
    private final VersionNMSEntityHelper helper;
    private String customName;
    
    public EntityNMSArmorStand(World world, StandardHologramLine parentHologramLine, ProtocolPacketSettings protocolPacketSettings) {
        super(world);
        this.parentHologramLine = parentHologramLine;
        this.protocolPacketSettings = protocolPacketSettings;
        this.helper = new VersionNMSEntityHelper(this);
        
        super.setInvisible(true);
        super.setSmall(true);
        super.setArms(false);
        super.setNoGravity(true);
        super.setBasePlate(true);
        super.setMarker(true);
        super.collides = false;
        super.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet
        forceSetBoundingBox(new NullBoundingBox());
    }
    
    @Override
    public void tick() {
        // Disable normal ticking for this entity
        
        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity
        if (super.onGround) {
            super.onGround = false;
        }
    }
    
    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity
        
        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity
        if (super.onGround) {
            super.onGround = false;
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
    public void setCustomName(IChatBaseComponent ichatbasecomponent) {
        // Prevents changes to custom name
    }
    
    @Override
    public void setCustomNameVisible(boolean visible) {
        // Prevents changes to custom name visibility
    }

    @Override
    public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
        // Prevent armor stand from being equipped
        return EnumInteractionResult.PASS;
    }

    @Override
    public boolean c(int i, ItemStack item) {
        // Prevent armor stand from being equipped
        return false;
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        // Prevent armor stand from being equipped
    }
    
    @Override
    public void a(AxisAlignedBB boundingBox) {
        // Prevent changes to bounding box
    }
    
    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
    }
    
    @Override
    public void a(SoundEffect soundeffect, float f, float f1) {
        // Remove sounds
    }
    
    @Override
    public void setCustomNameNMS(String customName) {
        if (Objects.equals(this.customName, customName)) {
            return;
        }
        this.customName = customName;
        super.setCustomName(createCustomNameNMSObject(customName));
        super.setCustomNameVisible(customName != null && !customName.isEmpty());
    }

    protected static IChatBaseComponent createCustomNameNMSObject(String customName) {
        return CraftChatMessage.fromStringOrNull(Strings.truncate(customName, 300));
    }
    
    @Override
    public String getCustomNameStringNMS() {
        return this.customName;
    }
    
    @Override
    public IChatBaseComponent getCustomNameObjectNMS() {
        return super.getCustomName();
    }
    
    @Override
    public void die() {
        // Prevent entity from dying
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
            helper.broadcastPacket(new PacketPlayOutEntityTeleport(this));
        }
    }

    @Override
    public void setPassengerNMS(NMSEntity passenger) {
        Preconditions.checkArgument(passenger instanceof Entity);
        Entity passengerEntity = (Entity) passenger;
        Preconditions.checkArgument(passengerEntity.getVehicle() == null);
        Preconditions.checkState(super.passengers.isEmpty());

        try {
            VEHICLE_FIELD.set(passenger, this);
            this.passengers.add(passengerEntity);
        } catch (ReflectiveOperationException e) {
            DebugLogger.cannotSetPassenger(e);
        }
    }

    @Override
    public boolean isDeadNMS() {
        return super.dead;
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
