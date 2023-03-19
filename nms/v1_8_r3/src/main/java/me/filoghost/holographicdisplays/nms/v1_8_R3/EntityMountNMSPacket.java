/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;

class EntityMountNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityMountNMSPacket(EntityID vehicleEntityID, EntityID passengerEntityID) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeInt(passengerEntityID.getNumericID());
        packetByteBuffer.writeInt(vehicleEntityID.getNumericID());

        packetByteBuffer.writeByte(0); // Leash

        this.rawPacket = packetByteBuffer.writeDataTo(new PacketPlayOutAttachEntity());
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
