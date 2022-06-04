/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

public interface EditableHologramLine {

    void setCoordinates(double x, double y, double z);

    double getHeight();

    void setDeleted();

}
