/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.internal;

import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextLine;

public class InternalTextLine extends BaseTextLine implements InternalHologramLine {

    private final String serializedConfigValue;

    protected InternalTextLine(InternalHologram hologram, String text, String serializedConfigValue) {
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

}
