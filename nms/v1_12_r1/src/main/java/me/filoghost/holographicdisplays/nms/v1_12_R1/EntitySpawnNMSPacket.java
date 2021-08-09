/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_12_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;

class EntitySpawnNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntitySpawnNMSPacket(EntityID entityID, int entityTypeID, double positionX, double positionY, double positionZ) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());
        packetByteBuffer.writeUUID(entityID.getUUID());
        packetByteBuffer.writeByte(entityTypeID);

        // Position
        packetByteBuffer.writeDouble(positionX);
        packetByteBuffer.writeDouble(positionY);
        packetByteBuffer.writeDouble(positionZ);

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // Object data
        if (entityTypeID == EntityTypeID.ITEM) {
            packetByteBuffer.writeInt(1); // Velocity is present and zero (otherwise by default a random velocity is applied)
        } else {
            packetByteBuffer.writeInt(0);
        }

        // Velocity
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);

        this.rawPacket = writeData(new PacketPlayOutSpawnEntity(), packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
