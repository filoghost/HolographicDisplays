/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.internal;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;
import org.bukkit.entity.Player;

public class InternalTextHologramLine extends BaseTextHologramLine implements InternalHologramLine {

    private final String serializedConfigValue;

    protected InternalTextHologramLine(InternalHologram hologram, String text, String serializedConfigValue) {
        super(hologram, text);
        this.serializedConfigValue = serializedConfigValue;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return true;
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

}
