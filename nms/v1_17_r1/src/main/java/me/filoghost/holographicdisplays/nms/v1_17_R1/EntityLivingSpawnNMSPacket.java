/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;

class EntityLivingSpawnNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityLivingSpawnNMSPacket(EntityID entityID, int entityTypeID, PositionCoordinates position, double positionOffsetY) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());
        packetByteBuffer.writeUUID(entityID.getUUID());
        packetByteBuffer.writeVarInt(entityTypeID);

        // Position
        packetByteBuffer.writeDouble(position.getX());
        packetByteBuffer.writeDouble(position.getY() + positionOffsetY);
        packetByteBuffer.writeDouble(position.getZ());

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // Head rotation
        packetByteBuffer.writeByte(0);

        // Velocity
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);

        this.rawPacket = new PacketPlayOutSpawnEntityLiving(packetByteBuffer.getInternalSerializer());
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
