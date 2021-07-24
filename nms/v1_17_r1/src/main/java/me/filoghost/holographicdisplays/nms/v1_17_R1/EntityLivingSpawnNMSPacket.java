/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;

class EntityLivingSpawnNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityLivingSpawnNMSPacket(EntityID entityID, int entityTypeID, double locationX, double locationY, double locationZ) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());
        packetByteBuffer.writeUUID(entityID.getUUID());
        packetByteBuffer.writeVarInt(entityTypeID);

        // Position
        packetByteBuffer.writeDouble(locationX);
        packetByteBuffer.writeDouble(locationY);
        packetByteBuffer.writeDouble(locationZ);

        // Rotation
        packetByteBuffer.writeByte(0);
        packetByteBuffer.writeByte(0);

        // Head rotation
        packetByteBuffer.writeByte(0);

        // Velocity
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);
        packetByteBuffer.writeShort(0);

        this.rawPacket = new PacketPlayOutSpawnEntityLiving(packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
