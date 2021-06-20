/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.protocollib.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.reflect.IntEnum;
import me.filoghost.holographicdisplays.plugin.util.NMSVersion;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerSpawnEntity extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY;
        
    private static PacketConstructor entityConstructor;
    
    /**
     * Represents the different object types.
     *
     * @author Kristian
     */
    public static class ObjectTypes extends IntEnum {
        public static final int BOAT = 1;
        public static final int ITEM_STACK = 2;
        public static final int MINECART = 10;
        public static final int MINECART_STORAGE = 11;
        public static final int MINECART_POWERED = 12;
        public static final int ACTIVATED_TNT = 50;
        public static final int ENDER_CRYSTAL = 51;
        public static final int ARROW_PROJECTILE = 60;
        public static final int SNOWBALL_PROJECTILE = 61;
        public static final int EGG_PROJECTILE = 62;
        public static final int FIRE_BALL_GHAST = 63;
        public static final int FIRE_BALL_BLAZE = 64;
        public static final int THROWN_ENDERPEARL = 65;
        public static final int WITHER_SKULL = 66;
        public static final int FALLING_BLOCK = 70;
        public static final int ITEM_FRAME = 71;
        public static final int EYE_OF_ENDER = 72;
        public static final int THROWN_POTION = 73;
        public static final int FALLING_DRAGON_EGG = 74;
        public static final int THROWN_EXP_BOTTLE = 75;
        public static final int FIREWORK = 76;
        public static final int ARMOR_STAND = 78;
        public static final int FISHING_FLOAT = 90;

        /**
         * The singleton instance. Can also be retrieved from the parent class.
         */
        private static final ObjectTypes INSTANCE = new ObjectTypes();
        
        /**
         * Retrieve an instance of the object types enum.
         */
        public static ObjectTypes getInstance() {
            return INSTANCE;
        }
    }
    
    public WrapperPlayServerSpawnEntity() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntity(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    public WrapperPlayServerSpawnEntity(Entity entity, int type, int objectData) {
        super(fromEntity(entity, type, objectData), TYPE);
    }
    
    // Useful constructor
    private static PacketContainer fromEntity(Entity entity, int type, int objectData) {
        if (entityConstructor == null) {
            if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_14_R1)) {
                entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, entity, objectData);
            } else {
                entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, entity, type, objectData);
            }
        }
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_14_R1)) {
            return entityConstructor.createPacket(entity, objectData);
        } else {
            return entityConstructor.createPacket(entity, type, objectData);
        }
    }
    
    /**
     * Retrieve entity ID of the Object.
    */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Retrieve the entity that will be spawned.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity that will be spawned.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }
    
    /**
     * Set entity ID of the Object.
    */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the type of object. See {@link ObjectTypes}
    */
    public int getType() {
        return handle.getIntegers().read(6);
    }
    
    /**
     * Set the type of object. See {@link ObjectTypes}.
    */
    public void setType(int value) {
        handle.getIntegers().write(6, value);
    }
    
}
