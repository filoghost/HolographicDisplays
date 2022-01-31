/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroup;
import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import me.filoghost.holographicdisplays.plugin.tick.CachedPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Viewer {

    private final CachedPlayer player;

    Viewer(CachedPlayer player) {
        this.player = player;
    }

    public final Player getBukkitPlayer() {
        return player.getBukkitPlayer();
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public void sendPackets(PacketGroup packetGroup) {
        packetGroup.sendTo(player.getBukkitPlayer());
    }

    public void sendIndividualPackets(IndividualTextPacketGroup packetGroup, String text) {
        packetGroup.sendTo(player.getBukkitPlayer(), text);
    }

}
