/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common.entity;

import me.filoghost.holographicdisplays.common.Position;
import me.filoghost.holographicdisplays.nms.common.NMSPacketList;
import org.bukkit.inventory.ItemStack;

public interface ItemNMSPacketEntity extends NMSPacketEntity {

    double ITEM_Y_OFFSET = 0;
    double ITEM_HEIGHT = 0.7;

    void addSpawnPackets(NMSPacketList packetList, Position position, ItemStack itemStack);

    void addChangePackets(NMSPacketList packetList, ItemStack itemStack);

}
