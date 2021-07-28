/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface StandardHologramLine extends StandardHologramComponent {

    void setLocation(World world, double x, double y, double z);

    double getHeight();

    void setChanged();

    boolean isVisibleTo(Player player);

}
