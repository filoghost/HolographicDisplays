/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.EditableHologramLine;
import org.bukkit.plugin.Plugin;

interface APIHologramLine extends HologramLine, EditableHologramLine {

    void setChanged();

    Plugin getCreatorPlugin();

}
