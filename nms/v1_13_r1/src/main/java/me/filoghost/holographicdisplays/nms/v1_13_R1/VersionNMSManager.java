/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_13_R1;

import me.filoghost.holographicdisplays.common.nms.EntityID;
import me.filoghost.holographicdisplays.common.nms.PacketListener;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.NMSPacketList;
import org.bukkit.entity.Player;

public class VersionNMSManager implements NMSManager {

    @Override
    public EntityID newEntityID() {
        return null;
    }

    @Override
    public NMSPacketList createPacketList() {
        return null;
    }

    @Override
    public void injectPacketListener(Player player, PacketListener packetListener) {

    }

    @Override
    public void uninjectPacketListener(Player player) {

    }

}
