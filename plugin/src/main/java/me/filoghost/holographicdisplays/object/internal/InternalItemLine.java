/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.object.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

public class InternalItemLine extends BaseItemLine implements InternalHologramLine {

    private final String serializedConfigValue;

    public InternalItemLine(StandardHologram parent, ItemStack itemStack, String serializedConfigValue) {
        super(parent, itemStack);
        this.serializedConfigValue = serializedConfigValue;
    }

    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

}
