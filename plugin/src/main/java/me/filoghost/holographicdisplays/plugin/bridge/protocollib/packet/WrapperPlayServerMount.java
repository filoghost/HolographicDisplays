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

public class WrapperPlayServerMount extends AbstractPacket {
    
    public static final PacketType TYPE = PacketType.Play.Server.MOUNT;
    
    public WrapperPlayServerMount() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerMount(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve the entity ID being attached.
    */
    public int getVehicleId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the entity ID being attached.
    */
    public void setVehicleId(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve the IDs of the passenger entities.
    */
    public List<Integer> getPassengers() {
        return Ints.asList(handle.getIntegerArrays().read(0));
    }
    
    /**
     * Set the passenger entities.
    */
    public void setPassengers(int[] entities) {
        handle.getIntegerArrays().write(0, entities);
    }
    
    /**
     * Set the passenger entities.
    */
    public void setPassengers(List<Integer> entities) {
        setPassengers(Ints.toArray(entities));
    }
}
