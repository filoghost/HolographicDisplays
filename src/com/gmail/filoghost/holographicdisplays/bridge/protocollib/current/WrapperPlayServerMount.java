/*
 *  PacketWrapper - Contains wrappers for each packet in Minecraft.
 *  Copyright (C) 2012 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the
 *  GNU Lesser General Public License as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program;
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307 USA
 */

package com.gmail.filoghost.holographicdisplays.bridge.protocollib.current;

import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.primitives.Ints;

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
     * Retrieve the player entity ID being attached.
     * @return The current Entity ID
    */
    public int getVehicleId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set the player entity ID being attached.
     * @param value - new value.
    */
    public void setVehicleId(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve the IDs of the entities that will be destroyed.
     * @return The current entities.
    */
    public List<Integer> getPassengers() {
        return Ints.asList(handle.getIntegerArrays().read(0));
    }
    
    /**
     * Set the entities that will be destroyed.
     * @param value - new value.
    */
    public void setPassengers(int[] entities) {
        handle.getIntegerArrays().write(0, entities);
    }
    
    /**
     * Set the entities that will be destroyed.
     * @param value - new value.
    */
    public void setPassengers(List<Integer> entities) {
    	setPassengers(Ints.toArray(entities));
    }
}