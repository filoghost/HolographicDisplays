/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import org.bukkit.entity.Player;

class TrackedPlayer {

    private final Player player;

    TrackedPlayer(Player player) {
        this.player = player;
    }

    public final Player getPlayer() {
        return player;
    }

    public void sendPackets(PacketGroup packetGroup) {
        packetGroup.sendTo(player);
    }

}
