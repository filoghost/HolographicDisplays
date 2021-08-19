/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms.entity;

import me.filoghost.holographicdisplays.common.Position;
import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;

public interface ClickableNMSPacketEntity extends NMSPacketEntity {

    double SLIME_Y_OFFSET = 0;
    double SLIME_HEIGHT = 0.5;

    EntityID getID();

    void addSpawnPackets(NMSPacketList packetList, Position position);

}
