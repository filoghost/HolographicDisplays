/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;

class EntityMetadataNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityMetadataNMSPacket(EntityID entityID, DataWatcherEntry<?>... dataWatcherEntries) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        packetByteBuffer.writeVarInt(entityID.getNumericID());
        packetByteBuffer.writeDataWatcherEntries(dataWatcherEntries);

        this.rawPacket = new PacketPlayOutEntityMetadata(packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
