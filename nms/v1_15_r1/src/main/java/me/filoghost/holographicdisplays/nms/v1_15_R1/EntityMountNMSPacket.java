/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_15_R1;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayOutMount;

class EntityMountNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityMountNMSPacket(EntityID vehicleEntityID, EntityID passengerEntityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(vehicleEntityID.getNumericID());
        packetByteBuffer.writeVarIntArray(passengerEntityID.getNumericID());

        this.rawPacket = packetByteBuffer.writeDataTo(new PacketPlayOutMount());
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
