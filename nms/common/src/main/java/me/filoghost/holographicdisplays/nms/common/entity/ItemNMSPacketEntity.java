/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common.entity;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import org.bukkit.inventory.ItemStack;

public interface ItemNMSPacketEntity extends NMSPacketEntity {

    double ITEM_Y_OFFSET = 0;
    double ITEM_HEIGHT = 0.7;

    PacketGroup newSpawnPackets(PositionCoordinates position, ItemStack itemStack);

    PacketGroup newChangePackets(ItemStack itemStack);

}
