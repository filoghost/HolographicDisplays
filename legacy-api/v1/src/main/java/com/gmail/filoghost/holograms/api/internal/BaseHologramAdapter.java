/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api.internal;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

class BaseHologramAdapter {

    private final String ownerPluginName;
    protected final me.filoghost.holographicdisplays.api.Hologram hologram;

    BaseHologramAdapter(Plugin plugin, Location source) {
        this.ownerPluginName = plugin.getName();
        this.hologram = me.filoghost.holographicdisplays.api.HolographicDisplaysAPI.get(plugin).createHologram(source);
    }

    protected void restrictVisibityTo(List<Player> whoCanSee) {
        hologram.getVisibilityManager().setVisibleByDefault(false);
        if (whoCanSee != null) {
            for (Player player : whoCanSee) {
                hologram.getVisibilityManager().showTo(player);
            }
        }
    }

    String getOwnerPluginName() {
        return ownerPluginName;
    }

}
