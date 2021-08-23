/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

public class APIItemLine extends BaseItemLine implements ItemHologramLine, APIClickableLine {

    public APIItemLine(APIHologram parent, ItemStack itemStack) {
        super(parent, itemStack);
    }

}
