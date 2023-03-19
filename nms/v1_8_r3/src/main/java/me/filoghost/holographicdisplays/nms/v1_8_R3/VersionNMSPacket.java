/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_8_R3;

import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

abstract class VersionNMSPacket implements PacketGroup {

    @Override
    public void sendTo(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getRawPacket());
    }

    abstract Packet<?> getRawPacket();

}
