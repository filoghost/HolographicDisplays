/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.api.hologram.Hologram;

public abstract class InternalHologramLine {

    private final String serializedString;

    public InternalHologramLine(String serializedString) {
        this.serializedString = serializedString;
    }

    public String getSerializedString() {
        return serializedString;
    }

    public abstract void appendTo(Hologram hologram);

}
