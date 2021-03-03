/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.base.BaseItemLine;
import org.bukkit.inventory.ItemStack;

public class InternalItemLine extends BaseItemLine implements InternalHologramLine {

    private final String serializedConfigValue;

    protected InternalItemLine(StandardHologram hologram, NMSManager nmsManager, ItemStack itemStack, String serializedConfigValue) {
        super(hologram, nmsManager, itemStack);
        this.serializedConfigValue = serializedConfigValue;
    }

    @Override
    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

}
