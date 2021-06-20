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
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class WrapperPlayServerSpawnEntityLiving extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_LIVING;
    
    private static PacketConstructor entityConstructor;
    
    public WrapperPlayServerSpawnEntityLiving() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntityLiving(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    public WrapperPlayServerSpawnEntityLiving(Entity entity) {
        super(fromEntity(entity), TYPE);
    }
    
    // Useful constructor
    private static PacketContainer fromEntity(Entity entity) {
        if (entityConstructor == null) {
            entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, entity);
        }
        return entityConstructor.createPacket(entity);
    }
    
    /**
     * Retrieve entity ID.
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
     * Set entity ID.
    */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve the type of mob.
    */
    @SuppressWarnings("deprecation")
    public EntityType getType() {
        return EntityType.fromId(handle.getIntegers().read(1));
    }
    
    /**
     * Set the type of mob.
    */
    @SuppressWarnings("deprecation")
    public void setType(EntityType value) {
        handle.getIntegers().write(1, (int) value.getTypeId());
    }
    
    /**
     * Retrieve the data watcher.
     * <p>
     * Content varies by mob, see Entities.
    */
    public WrappedDataWatcher getMetadata() {
        return handle.getDataWatcherModifier().read(0);
    }
    
    /**
     * Set the data watcher.
    */
    public void setMetadata(WrappedDataWatcher value) {
        handle.getDataWatcherModifier().write(0, value);
    }
}
