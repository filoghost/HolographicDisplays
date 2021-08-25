/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;
import me.filoghost.holographicdisplays.nms.common.entity.ItemNMSPacketEntity;
import me.filoghost.holographicdisplays.nms.common.entity.TextNMSPacketEntity;
import org.bukkit.entity.Player;

public interface NMSManager {

    TextNMSPacketEntity newTextPacketEntity();

    ItemNMSPacketEntity newItemPacketEntity();

    ClickableNMSPacketEntity newClickablePacketEntity();

    void injectPacketListener(Player player, PacketListener packetListener);

    void uninjectPacketListener(Player player);

}
