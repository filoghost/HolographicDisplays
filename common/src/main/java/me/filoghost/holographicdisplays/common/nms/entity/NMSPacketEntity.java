/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms.entity;

import me.filoghost.holographicdisplays.common.Position;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;

public interface NMSPacketEntity {

    void addTeleportPackets(NMSPacketList packetList, Position position);

    void addDestroyPackets(NMSPacketList packetList);

}
