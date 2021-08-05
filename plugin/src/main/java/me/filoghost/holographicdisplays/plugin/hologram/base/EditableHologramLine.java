/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

public interface EditableHologramLine {

    void setLocation(double x, double y, double z);

    double getHeight();

    void setDeleted();

}
