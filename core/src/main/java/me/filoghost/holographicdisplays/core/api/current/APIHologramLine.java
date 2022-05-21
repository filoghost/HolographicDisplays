/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.core.base.EditableHologramLine;
import org.bukkit.plugin.Plugin;

interface APIHologramLine extends HologramLine, EditableHologramLine {

    void setChanged();

    Plugin getCreatorPlugin();

}
