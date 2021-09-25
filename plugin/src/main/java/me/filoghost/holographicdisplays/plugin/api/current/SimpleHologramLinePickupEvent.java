/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.hologram.line.HologramLinePickupEvent;
import org.bukkit.entity.Player;

class SimpleHologramLinePickupEvent implements HologramLinePickupEvent {

    private final Player player;

    SimpleHologramLinePickupEvent(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}
