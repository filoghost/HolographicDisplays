/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class IndividualNMSPacket implements NMSPacket {

    private final Function<Player, NMSPacket> individualPacketFactory;

    public IndividualNMSPacket(Function<Player, NMSPacket> individualPacketFactory) {
        this.individualPacketFactory = individualPacketFactory;
    }

    @Override
    public void sendTo(Player player) {
        individualPacketFactory.apply(player).sendTo(player);
    }

}
