/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.EditableHologramLine;

public interface APIHologramLine extends HologramLine, EditableHologramLine {

    void setChanged();

}
