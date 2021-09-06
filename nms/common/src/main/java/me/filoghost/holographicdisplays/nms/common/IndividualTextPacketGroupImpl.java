/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import org.bukkit.entity.Player;

import java.util.Objects;

class IndividualTextPacketGroupImpl {

    static class IndividualTextPacketGroup1 implements IndividualTextPacketGroup {

        private final IndividualTextPacketGroupFactory packetFactory;
        private PacketGroup packet;
        private String lastPacketText;

        IndividualTextPacketGroup1(IndividualTextPacketGroupFactory packetFactory) {
            this.packetFactory = packetFactory;
        }

        @Override
        public void sendTo(Player player, String text) {
            if (packet == null || !Objects.equals(lastPacketText, text)) {
                packet = packetFactory.createPacket(text);
                lastPacketText = text;
            }
            packet.sendTo(player);
        }

    }

    static class IndividualTextPacketGroup2 extends IndividualTextPacketGroup1 implements IndividualTextPacketGroup {

        private final PacketGroup packet1;

        IndividualTextPacketGroup2(PacketGroup packet1, IndividualTextPacketGroupFactory packet2Factory) {
            super(packet2Factory);
            this.packet1 = packet1;
        }

        @Override
        public void sendTo(Player player, String text) {
            packet1.sendTo(player);
            super.sendTo(player, text);
        }

    }

}
