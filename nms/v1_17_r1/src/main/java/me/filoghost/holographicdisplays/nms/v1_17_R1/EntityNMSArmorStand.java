/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import com.google.common.collect.ImmutableList;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.reflection.ReflectField;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.common.nms.ProtocolPacketSettings;
import me.filoghost.holographicdisplays.common.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.common.nms.entity.NMSEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.Objects;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {

    private static final ReflectField<Entity> VEHICLE_FIELD = ReflectField.lookup(Entity.class, Entity.class, "au");

    private final StandardHologramLine parentHologramLine;
    private final ProtocolPacketSettings protocolPacketSettings;
    private final VersionNMSEntityHelper helper;
    private CraftEntity customBukkitEntity;
    private String customName;

    public EntityNMSArmorStand(World world, StandardHologramLine parentHologramLine, ProtocolPacketSettings protocolPacketSettings) {
        super(EntityTypes.c /* ARMOR_STAND */, world);
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
        super.z /* onGround */ = true; // Workaround to force EntityTrackerEntry to send a teleport packet
        forceSetBoundingBox(new NullBoundingBox());
    }

    @Override
    public void tick() {
        // Disable normal ticking for this entity

        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity
        if (super.z /* onGround */) {
            super.z = false;
        }
    }

    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity

        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity
        if (super.z /* onGround */) {
            super.z = false;
        }
    }

    @Override
    public void setPosition(double d0, double d1, double d2) {
        // Do not change it!
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
        return EnumInteractionResult.d /* PASS */;
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        // Prevent armor stand from being equipped
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack, boolean silent) {
        // Prevent armor stand from being equipped
    }

    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
    }

    @Override
    public void playSound(SoundEffect soundeffect, float f, float f1) {
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
    public void killEntity() {
        // Prevent entity from dying
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (customBukkitEntity == null) {
            customBukkitEntity = new CraftNMSArmorStand(super.getWorld().getCraftServer(), this);
        }
        return customBukkitEntity;
    }

    @Override
    public void killEntityNMS() {
        super.setRemoved(RemovalReason.b /* DISCARDED */);
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
        Preconditions.checkState(super.getPassengers().isEmpty());

        try {
            VEHICLE_FIELD.set(passenger, this);
            ((Entity) this).at /* passengers */ = ImmutableList.of(this);
        } catch (ReflectiveOperationException e) {
            DebugLogger.cannotSetPassenger(e);
        }
    }

    @Override
    public boolean isDeadNMS() {
        return super.isRemoved();
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
