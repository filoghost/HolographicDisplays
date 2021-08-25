/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseItemHologramLine;
import org.bukkit.entity.Player;
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

    @Override
    public boolean hasClickCallback() {
        return false;
    }

    @Override
    public void invokeClickCallback(Player player) {}

    @Override
    public boolean hasPickupCallback() {
        return false;
    }

    @Override
    public void invokePickupCallback(Player player) {}

}
