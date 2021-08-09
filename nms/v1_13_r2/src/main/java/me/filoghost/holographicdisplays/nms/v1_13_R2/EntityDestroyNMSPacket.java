/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_13_R2;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;

class EntityDestroyNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityDestroyNMSPacket(EntityID... entityIDs) {
        int[] entityIDsArray = new int[entityIDs.length];
        for (int i = 0; i < entityIDs.length; i++) {
            entityIDsArray[i] = entityIDs[i].getNumericID();
        }

        this.rawPacket = new PacketPlayOutEntityDestroy(entityIDsArray);
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
