/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;

class EntityListDestroyNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityListDestroyNMSPacket(EntityID... entityIDs) {
        PacketByteBuffer packetByteBuffer = PacketByteBuffer.get();

        IntArrayList entityIDsList = new IntArrayList(entityIDs.length);
        for (EntityID entityID : entityIDs) {
            entityIDsList.add(entityID.getNumericID());
        }
        packetByteBuffer.writeIntList(entityIDsList);

        this.rawPacket = new PacketPlayOutEntityDestroy(packetByteBuffer);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
