/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.protocollib.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.primitives.Ints;

import java.util.List;

public class WrapperPlayServerEntityDestroy extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_DESTROY;
    
    public WrapperPlayServerEntityDestroy() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityDestroy(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the IDs of the entities that will be destroyed.
    */
    public List<Integer> getEntities() {
        return Ints.asList(handle.getIntegerArrays().read(0));
    }
    
    /**
     * Set the entities that will be destroyed.
    */
    public void setEntities(int[] entities) {
        handle.getIntegerArrays().write(0, entities);
    }
    
    /**
     * Set the entities that will be destroyed.
    */
    public void setEntities(List<Integer> entities) {
        setEntities(Ints.toArray(entities));
    }
}
