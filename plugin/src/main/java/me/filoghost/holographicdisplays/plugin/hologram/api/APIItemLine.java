/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class APIItemLine extends BaseItemLine implements ItemHologramLine, APIClickableLine {

    private final APIHologram parent;

    public APIItemLine(APIHologram parent, ItemStack itemStack) {
        super(parent, itemStack);
        this.parent = parent;
    }

    @Override
    public @NotNull APIHologram getHologram() {
        return parent;
    }

}
