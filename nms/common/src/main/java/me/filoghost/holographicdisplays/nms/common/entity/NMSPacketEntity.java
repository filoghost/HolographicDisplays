/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common.entity;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.NMSPacketList;

public interface NMSPacketEntity {

    void addTeleportPackets(NMSPacketList packetList, PositionCoordinates position);

    void addDestroyPackets(NMSPacketList packetList);

}
