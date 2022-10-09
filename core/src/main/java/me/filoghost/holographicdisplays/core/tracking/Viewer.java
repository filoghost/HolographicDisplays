/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.holographicdisplays.nms.common.IndividualTextPacketGroup;
import me.filoghost.holographicdisplays.nms.common.PacketGroup;
import me.filoghost.holographicdisplays.core.tick.CachedPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

class Viewer {

    private final CachedPlayer player;

    Viewer(CachedPlayer player) {
        this.player = player;
    }

    public final Player getBukkitPlayer() {
        return player.getBukkitPlayer();
    }

    public @Nullable Location getLocation() {
        return player.getLocation();
    }

    public void sendPackets(PacketGroup packetGroup) {
        packetGroup.sendTo(player.getBukkitPlayer());
    }

    public void sendIndividualPackets(IndividualTextPacketGroup packetGroup, String text) {
        packetGroup.sendTo(player.getBukkitPlayer(), text);
    }

}
