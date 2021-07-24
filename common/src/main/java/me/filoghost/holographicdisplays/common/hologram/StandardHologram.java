/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface StandardHologram extends StandardHologramComponent {

    List<? extends StandardHologramLine> getLines();

    int getLineCount();

    Plugin getCreatorPlugin();

    boolean isVisibleTo(Player player);

}
