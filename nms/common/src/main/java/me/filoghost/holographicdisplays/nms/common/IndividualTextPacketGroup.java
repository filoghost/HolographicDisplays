/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroupImpl.IndividualTextPacketGroup1;
import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroupImpl.IndividualTextPacketGroup2;
import org.bukkit.entity.Player;

public interface IndividualTextPacketGroup {

    void sendTo(Player player, String text);

    static IndividualTextPacketGroup of(IndividualTextPacketGroupFactory packet) {
        return new IndividualTextPacketGroup1(packet);
    }

    static IndividualTextPacketGroup of(PacketGroup packet1, IndividualTextPacketGroupFactory packet2) {
        return new IndividualTextPacketGroup2(packet1, packet2);
    }


    @FunctionalInterface
    interface IndividualTextPacketGroupFactory {

        PacketGroup createPacket(String text);

    }

}
