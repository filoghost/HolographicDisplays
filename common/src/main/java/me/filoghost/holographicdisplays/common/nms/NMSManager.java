/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms;

import me.filoghost.holographicdisplays.common.nms.entity.ClickableNMSPacketEntity;
import me.filoghost.holographicdisplays.common.nms.entity.ItemNMSPacketEntity;
import me.filoghost.holographicdisplays.common.nms.entity.TextNMSPacketEntity;
import org.bukkit.entity.Player;

public interface NMSManager {

    TextNMSPacketEntity newTextPacketEntity();

    ItemNMSPacketEntity newItemPacketEntity();

    ClickableNMSPacketEntity newClickablePacketEntity();

    void injectPacketListener(Player player, PacketListener packetListener);

    void uninjectPacketListener(Player player);

}
