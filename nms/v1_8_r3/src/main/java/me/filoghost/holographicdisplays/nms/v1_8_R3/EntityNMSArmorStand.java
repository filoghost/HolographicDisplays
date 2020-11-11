/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.common.ConsoleLogger;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.common.reflection.ReflectField;
import me.filoghost.holographicdisplays.common.reflection.ReflectMethod;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.Vec3D;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

import java.util.logging.Level;

public class EntityNMSArmorStand extends EntityArmorStand implements NMSArmorStand {
    
    private static final ReflectField<Integer> DISABLED_SLOTS_FIELD = new ReflectField<>(EntityArmorStand.class, "bi");
    private static final ReflectMethod<Void> SET_MARKER_METHOD = new ReflectMethod<>(EntityArmorStand.class, "n", boolean.class);

    private HologramLine parentPiece;
    private String customName;
    
    public EntityNMSArmorStand(World world, HologramLine parentPiece) {
        super(world);
        setInvisible(true);
        setSmall(true);
        setArms(false);
        setGravity(true);
        setBasePlate(true);
        try {
            SET_MARKER_METHOD.invoke(this, true);
        } catch (Throwable t) {
            ConsoleLogger.logDebug(Level.SEVERE, "Couldn't set armor stand as marker", t);
            // It will still work, but the offset will be wrong.
        }
        this.parentPiece = parentPiece;
        try {
            DISABLED_SLOTS_FIELD.set(this, Integer.MAX_VALUE);
        } catch (Exception e) {
            // There's still the overridden method.
        }
        forceSetBoundingBox(new NullBoundingBox());
        
        this.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
    }
    
    @Override
    public void t_() {
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
        // Do not change it!
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
            this.bukkitEntity = new CraftNMSArmorStand(this.world.getServer(), this);
        }
        return this.bukkitEntity;
    }
    
    @Override
    public void killEntityNMS() {
        super.dead = true;
    }
    
    @Override
    public void setLocationNMS(double x, double y, double z, boolean broadcastLocationPacket) {
        super.setPosition(x, y, z);
        if (broadcastLocationPacket) {
            broadcastLocationPacketNMS();
        }
    }
    
    private void broadcastLocationPacketNMS() {
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this);

        for (Object obj : this.world.players) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                double distanceSquared = Utils.square(nmsPlayer.locX - this.locX) + Utils.square(nmsPlayer.locZ - this.locZ);
                if (distanceSquared < 8192 && nmsPlayer.playerConnection != null) {
                    nmsPlayer.playerConnection.sendPacket(teleportPacket);
                }
            }
        }
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
    public HologramLine getHologramLine() {
        return parentPiece;
    }
    
    @Override
    public org.bukkit.entity.Entity getBukkitEntityNMS() {
        return getBukkitEntity();
    }
}
