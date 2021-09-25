/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.hologram.line.HologramLineClickEvent;
import org.bukkit.entity.Player;

class SimpleHologramLineClickEvent implements HologramLineClickEvent {

    private final Player player;

    SimpleHologramLineClickEvent(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

}
