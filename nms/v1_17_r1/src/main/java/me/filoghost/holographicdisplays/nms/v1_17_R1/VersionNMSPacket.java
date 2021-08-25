/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_17_R1;

import me.filoghost.holographicdisplays.nms.common.NMSPacket;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

abstract class VersionNMSPacket implements NMSPacket {

    @Override
    public void sendTo(Player player) {
        ((CraftPlayer) player).getHandle().b.sendPacket(getRawPacket());
    }

    abstract Packet<?> getRawPacket();

}
