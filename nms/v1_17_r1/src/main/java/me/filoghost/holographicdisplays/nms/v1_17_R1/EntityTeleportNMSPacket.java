/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;

class EntityTeleportNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityTeleportNMSPacket(EntityID entityID, double positionX, double positionY, double positionZ) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());

        // Position
        packetByteBuffer.writeDouble(positionX);
        packetByteBuffer.writeDouble(positionY);
        packetByteBuffer.writeDouble(positionZ);

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // On ground
        packetByteBuffer.writeBoolean(false);

        this.rawPacket = new PacketPlayOutEntityTeleport(packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
