/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms.entity;

import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import org.bukkit.inventory.ItemStack;

public interface ItemNMSPacketEntity extends NMSPacketEntity {

    void addSpawnPackets(NMSPacketList packetList, double positionX, double positionY, double positionZ, ItemStack itemStack);

    void addChangePackets(NMSPacketList packetList, ItemStack itemStack);

}
