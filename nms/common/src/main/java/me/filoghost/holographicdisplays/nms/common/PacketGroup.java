/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import me.filoghost.holographicdisplays.nms.common.PacketGroupImpl.PacketGroup2;
import me.filoghost.holographicdisplays.nms.common.PacketGroupImpl.PacketGroup4;
import me.filoghost.holographicdisplays.nms.common.PacketGroupImpl.PacketGroup5;
import org.bukkit.entity.Player;

public interface PacketGroup {

    void sendTo(Player player);

    static PacketGroup of(PacketGroup packet1, PacketGroup packet2) {
        return new PacketGroup2(packet1, packet2);
    }

    static PacketGroup of(PacketGroup packet1, PacketGroup packet2, PacketGroup packet3, PacketGroup packet4) {
        return new PacketGroup4(packet1, packet2, packet3, packet4);
    }

    static PacketGroup of(PacketGroup packet1, PacketGroup packet2, PacketGroup packet3, PacketGroup packet4, PacketGroup packet5) {
        return new PacketGroup5(packet1, packet2, packet3, packet4, packet5);
    }

}
