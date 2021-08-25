/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common.entity;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.IndividualText;
import me.filoghost.holographicdisplays.nms.common.NMSPacketList;

public interface TextNMSPacketEntity extends NMSPacketEntity {

    double ARMOR_STAND_Y_OFFSET = -0.29;
    double ARMOR_STAND_TEXT_HEIGHT = 0.23;

    void addSpawnPackets(NMSPacketList packetList, PositionCoordinates position, String text);

    void addSpawnPackets(NMSPacketList packetList, PositionCoordinates position, IndividualText individualText);

    void addChangePackets(NMSPacketList packetList, String text);

    void addChangePackets(NMSPacketList packetList, IndividualText individualText);


}
