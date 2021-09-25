/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.EditableHologramLine;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("deprecation")
interface V2HologramLine extends HologramLine, EditableHologramLine {

    @Override
    V2Hologram getParent();

    @Override
    default void removeLine() {
        getParent().getLines().remove(this);
    }

    default Plugin getCreatorPlugin() {
        return getParent().getCreatorPlugin();
    }

}
