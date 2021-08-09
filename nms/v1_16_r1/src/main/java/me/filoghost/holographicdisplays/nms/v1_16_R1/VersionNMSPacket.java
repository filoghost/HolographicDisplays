/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_16_R1;

import me.filoghost.holographicdisplays.common.nms.NMSPacket;
import net.minecraft.server.v1_16_R1.Packet;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;

abstract class VersionNMSPacket implements NMSPacket {

    @Override
    public void sendTo(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getRawPacket());
    }

    abstract Packet<?> getRawPacket();

    protected static <T extends Packet<?>> T writeData(T packet, PacketByteBuffer packetByteBuffer) {
        try {
            packet.a(packetByteBuffer);
            return packet;
        } catch (IOException e) {
            // Never thrown by the implementations
            throw new RuntimeException(e);
        }
    }

}
