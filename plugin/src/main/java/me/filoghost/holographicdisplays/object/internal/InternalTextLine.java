/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.internal;

import me.filoghost.holographicdisplays.core.object.base.BaseHologram;
import me.filoghost.holographicdisplays.object.base.BaseTextLine;

public class InternalTextLine extends BaseTextLine implements InternalHologramLine {

    private final String serializedConfigValue;

    public InternalTextLine(BaseHologram parent, String text, String serializedConfigValue) {
        super(parent, text);
        this.serializedConfigValue = serializedConfigValue;
    }

    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

}
