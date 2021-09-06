/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import org.bukkit.entity.Player;

class PacketGroupImpl {

    static class PacketGroup2 implements PacketGroup {

        private final PacketGroup packet1;
        private final PacketGroup packet2;

        PacketGroup2(PacketGroup packet1, PacketGroup packet2) {
            this.packet1 = packet1;
            this.packet2 = packet2;
        }

        @Override
        public void sendTo(Player player) {
            packet1.sendTo(player);
            packet2.sendTo(player);
        }

    }

    static class PacketGroup4 implements PacketGroup {

        private final PacketGroup packet1;
        private final PacketGroup packet2;
        private final PacketGroup packet3;
        private final PacketGroup packet4;

        PacketGroup4(PacketGroup packet1, PacketGroup packet2, PacketGroup packet3, PacketGroup packet4) {
            this.packet1 = packet1;
            this.packet2 = packet2;
            this.packet3 = packet3;
            this.packet4 = packet4;
        }

        @Override
        public void sendTo(Player player) {
            packet1.sendTo(player);
            packet2.sendTo(player);
            packet3.sendTo(player);
            packet4.sendTo(player);
        }

    }

    static class PacketGroup5 implements PacketGroup {

        private final PacketGroup packet1;
        private final PacketGroup packet2;
        private final PacketGroup packet3;
        private final PacketGroup packet4;
        private final PacketGroup packet5;

        PacketGroup5(PacketGroup packet1, PacketGroup packet2, PacketGroup packet3, PacketGroup packet4, PacketGroup packet5) {
            this.packet1 = packet1;
            this.packet2 = packet2;
            this.packet3 = packet3;
            this.packet4 = packet4;
            this.packet5 = packet5;
        }

        @Override
        public void sendTo(Player player) {
            packet1.sendTo(player);
            packet2.sendTo(player);
            packet3.sendTo(player);
            packet4.sendTo(player);
            packet5.sendTo(player);
        }

    }

}
