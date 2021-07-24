/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import org.bukkit.World;

public interface StandardHologramLine extends StandardHologramComponent {

    StandardHologram getHologram();

    void setLocation(World world, double x, double y, double z);

    double getHeight();

}
