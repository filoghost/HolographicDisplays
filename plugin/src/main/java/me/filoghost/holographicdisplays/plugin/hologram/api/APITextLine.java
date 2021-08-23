/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextLine;

public class APITextLine extends BaseTextLine implements TextHologramLine, APIClickableLine {

    private final APIHologram parent;

    public APITextLine(APIHologram parent, String text) {
        super(parent, text);
        this.parent = parent;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return parent.isAllowPlaceholders();
    }

}
