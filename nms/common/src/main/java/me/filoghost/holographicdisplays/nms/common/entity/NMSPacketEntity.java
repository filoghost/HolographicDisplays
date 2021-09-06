/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common.entity;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.PacketGroup;

public interface NMSPacketEntity {

    PacketGroup newTeleportPackets(PositionCoordinates position);

    PacketGroup newDestroyPackets();

}
