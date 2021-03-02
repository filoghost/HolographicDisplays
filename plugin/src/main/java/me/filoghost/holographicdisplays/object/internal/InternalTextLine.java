/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.object.base.BaseTextLine;

public class InternalTextLine extends BaseTextLine implements InternalHologramLine {

    private final String serializedConfigValue;

    public InternalTextLine(StandardHologram parent, String text, String serializedConfigValue) {
        super(parent, text);
        this.serializedConfigValue = serializedConfigValue;
    }

    @Override
    protected boolean isAllowPlaceholders() {
        return true;
    }

    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

}
