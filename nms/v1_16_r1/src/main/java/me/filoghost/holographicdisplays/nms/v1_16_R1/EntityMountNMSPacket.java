/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayOutMount;

class EntityMountNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityMountNMSPacket(EntityID vehicleEntityID, EntityID passengerEntityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(vehicleEntityID.getNumericID());
        packetByteBuffer.writeIntArray(passengerEntityID.getNumericID());

        this.rawPacket = writeData(new PacketPlayOutMount(), packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
