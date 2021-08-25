/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.test;

import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.nms.common.PacketListener;
import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;
import me.filoghost.holographicdisplays.nms.common.entity.ItemNMSPacketEntity;
import me.filoghost.holographicdisplays.nms.common.entity.TextNMSPacketEntity;
import org.bukkit.entity.Player;

public class TestNMSManager implements NMSManager {

    @Override
    public TextNMSPacketEntity newTextPacketEntity() {
        return null;
    }

    @Override
    public ItemNMSPacketEntity newItemPacketEntity() {
        return null;
    }

    @Override
    public ClickableNMSPacketEntity newClickablePacketEntity() {
        return null;
    }

    @Override
    public void injectPacketListener(Player player, PacketListener packetListener) {

    }

    @Override
    public void uninjectPacketListener(Player player) {

    }

}
