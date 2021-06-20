/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.object.internal;

import me.filoghost.holographicdisplays.plugin.object.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

public class InternalItemLine extends BaseItemLine implements InternalHologramLine {

    private final String serializedConfigValue;

    protected InternalItemLine(InternalHologram hologram, ItemStack itemStack, String serializedConfigValue) {
        super(hologram, itemStack);
        this.serializedConfigValue = serializedConfigValue;
    }

    @Override
    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

}
