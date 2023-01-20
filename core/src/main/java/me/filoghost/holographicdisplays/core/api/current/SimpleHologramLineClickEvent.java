/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.holographicdisplays.api.hologram.line.HologramClickType;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLineClickEvent;
import org.bukkit.entity.Player;

class SimpleHologramLineClickEvent implements HologramLineClickEvent {

    private final Player player;
    private final HologramClickType clickType;

    SimpleHologramLineClickEvent(Player player, HologramClickType clickType) {
        this.player = player;
        this.clickType = clickType;
    }

    public SimpleHologramLineClickEvent(Player player) {
        this.player = player;
        this.clickType = HologramClickType.RIGHT_CLICK;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public HologramClickType getClickType() {
        return this.clickType;
    }

    @Override
    public String toString() {
        return "HologramLineClickEvent{"
               + "player=" + player
               + "clickType=" + clickType
               + "}";
    }

}
