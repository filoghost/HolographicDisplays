/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R3;

import me.filoghost.holographicdisplays.common.Position;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;

class EntityTeleportNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityTeleportNMSPacket(EntityID entityID, Position position, double positionOffsetY) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());

        // Position
        packetByteBuffer.writeDouble(position.getX());
        packetByteBuffer.writeDouble(position.getY() + positionOffsetY);
        packetByteBuffer.writeDouble(position.getZ());

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // On ground
        packetByteBuffer.writeBoolean(false);

        this.rawPacket = writeData(new PacketPlayOutEntityTeleport(), packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
