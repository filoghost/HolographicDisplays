/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.protocollib.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPacket {
    // The packet we will be modifying
    protected PacketContainer handle;

    /**
     * Constructs a new strongly typed wrapper for the given packet.
     */
    protected AbstractPacket(PacketContainer handle, PacketType type) {
        // Make sure we're given a valid packet
        if (handle == null) {
            throw new IllegalArgumentException("Packet handle cannot be NULL.");
        }
        if (!Objects.equal(handle.getType(), type)) {
            throw new IllegalArgumentException(handle.getHandle() + " is not a packet of type " + type);
        }

        this.handle = handle;
    }

    /**
     * Retrieve a handle to the raw packet data.
     */
    public PacketContainer getHandle() {
        return handle;
    }

    /**
     * Send the current packet to the given receiver.
     *
     * @throws RuntimeException If the packet cannot be sent.
     */
    public void sendPacket(Player receiver) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, getHandle());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet.", e);
        }
    }

    /**
     * Simulate receiving the current packet from the given sender.
     *
     * @throws RuntimeException If the packet cannot be received.
     */
    public void receivePacket(Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, getHandle());
        } catch (Exception e) {
            throw new RuntimeException("Cannot receive packet.", e);
        }
    }
}
