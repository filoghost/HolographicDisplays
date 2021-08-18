/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms.entity;

import me.filoghost.holographicdisplays.common.nms.NMSPacketList;

public interface NMSPacketEntity {

    void addTeleportPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ);

    void addDestroyPackets(NMSPacketList packetList);

}
