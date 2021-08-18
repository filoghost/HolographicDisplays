/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms.entity;

import me.filoghost.holographicdisplays.common.nms.IndividualText;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;

public interface TextNMSPacketEntity extends NMSPacketEntity {

    void addSpawnPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ);

    void addSpawnPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ, String text);

    void addSpawnPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ, IndividualText individualText);

    void addChangePackets(NMSPacketList packetList, String text);

    void addChangePackets(NMSPacketList packetList, IndividualText individualText);


}
