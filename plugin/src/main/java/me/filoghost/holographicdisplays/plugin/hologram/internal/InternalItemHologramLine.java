/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.internal;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemHologramLine;
import org.bukkit.inventory.ItemStack;

public class InternalItemHologramLine extends BaseItemHologramLine implements InternalHologramLine {

    private final String serializedConfigValue;

    protected InternalItemHologramLine(InternalHologram hologram, ItemStack itemStack, String serializedConfigValue) {
        super(hologram, itemStack);
        this.serializedConfigValue = serializedConfigValue;
    }

    @Override
    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

}
