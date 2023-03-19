/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_12_R1;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityTeleport;

class EntityTeleportNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityTeleportNMSPacket(EntityID entityID, PositionCoordinates position, double positionOffsetY) {
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

        this.rawPacket = packetByteBuffer.writeDataTo(new PacketPlayOutEntityTeleport());
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
