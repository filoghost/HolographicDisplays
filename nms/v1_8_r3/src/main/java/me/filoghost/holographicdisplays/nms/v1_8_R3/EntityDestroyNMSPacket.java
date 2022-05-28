/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import me.filoghost.holographicdisplays.nms.common.EntityID;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

class EntityDestroyNMSPacket extends VersionNMSPacket {

    private final Packet<?> rawPacket;

    EntityDestroyNMSPacket(EntityID entityID) {
        this.rawPacket = new PacketPlayOutEntityDestroy(entityID.getNumericID());
    }

    EntityDestroyNMSPacket(EntityID entityID1, EntityID entityID2) {
        this.rawPacket = new PacketPlayOutEntityDestroy(entityID1.getNumericID(), entityID2.getNumericID());
    }

    @Override
    Packet<?> getRawPacket() {
        return rawPacket;
    }

}
