/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_14_R1;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.core.Utils;
import me.filoghost.holographicdisplays.core.nms.PacketController;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import net.minecraft.server.v1_14_R1.AxisAlignedBB;
import net.minecraft.server.v1_14_R1.DamageSource;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.EnumInteractionResult;
import net.minecraft.server.v1_14_R1.EnumItemSlot;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_14_R1.SoundEffect;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {
    
    private final HologramLine parentPiece;
    private final PacketController packetController;
    private CraftEntity customBukkitEntity;
    private String customName;
    
    public EntityNMSArmorStand(World world, HologramLine parentPiece, PacketController packetController) {
        super(EntityTypes.ARMOR_STAND, world);
        super.setInvisible(true);
        super.setSmall(true);
        super.setArms(false);
        super.setNoGravity(true);
        super.setBasePlate(true);
        super.setMarker(true);
        super.collides = false;
        
        this.parentPiece = parentPiece;
        this.packetController = packetController;
        forceSetBoundingBox(new NullBoundingBox());
        this.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
    }
    
    @Override
    public void tick() {
        // Disable normal ticking for this entity.
        
        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if (this.onGround) {
            this.onGround = false;
        }
    }
    
    @Override
    public void inactiveTick() {
        // Disable normal ticking for this entity.
        
        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if (this.onGround) {
            this.onGround = false;
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
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
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
        // Locks the custom name.
    }
    
    @Override
    public void setCustomNameVisible(boolean visible) {
        // Locks the custom name.
    }

    @Override
    public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
        // Prevent stand being equipped
        return EnumInteractionResult.PASS;
    }

    @Override
    public boolean a_(int i, ItemStack item) {
        // Prevent stand being equipped
        return false;
    }

    @Override
    public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
        // Prevent stand being equipped
    }
    
    @Override
    public void a(AxisAlignedBB boundingBox) {
        // Do not change it!
    }
    
    public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
        super.a(boundingBox);
    }
    
    @Override
    public void a(SoundEffect soundeffect, float f, float f1) {
        // Remove sounds.
    }
    
    @Override
    public void setCustomNameNMS(String name) {
        this.customName = Utils.limitLength(name, 300);
        super.setCustomName(CraftChatMessage.fromStringOrNull(customName));
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
        if (customBukkitEntity == null) {
            customBukkitEntity = new CraftNMSArmorStand(super.world.getServer(), this);
        }
        return customBukkitEntity;
    }
    
    @Override
    public void killEntityNMS() {
        super.dead = true;
    }
    
    @Override
    public void setLocationNMS(double x, double y, double z) {
        super.setPosition(x, y, z);
        if (packetController.shouldBroadcastLocationPacket()) {
            broadcastLocationPacketNMS();
        }
    }
    
    private void broadcastLocationPacketNMS() {
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);
        
        for (Object obj : super.world.getPlayers()) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                double distanceSquared = Utils.square(nmsPlayer.locX - super.locX) + Utils.square(nmsPlayer.locZ - super.locZ);
                if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) {
                    nmsPlayer.playerConnection.sendPacket(teleportPacket);
                }
            }
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
    public HologramLine getHologramLine() {
        return parentPiece;
    }
    
    @Override
    public org.bukkit.entity.Entity getBukkitEntityNMS() {
        return getBukkitEntity();
    }
}
