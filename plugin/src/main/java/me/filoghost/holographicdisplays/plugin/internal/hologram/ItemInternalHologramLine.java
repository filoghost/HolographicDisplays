/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import org.bukkit.inventory.ItemStack;

public class ItemInternalHologramLine extends InternalHologramLine {

    private final ItemStack itemStack;

    public ItemInternalHologramLine(String serializedString, ItemStack itemStack) {
        super(serializedString);
        this.itemStack = itemStack;
    }

    @Override
    public void appendTo(Hologram hologram) {
        hologram.getLines().appendItem(itemStack);
    }

}
