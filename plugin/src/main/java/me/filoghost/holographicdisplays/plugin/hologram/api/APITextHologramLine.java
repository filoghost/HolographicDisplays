/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.holographicdisplays.api.hologram.line.ClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;
import org.jetbrains.annotations.Nullable;

public class APITextHologramLine extends BaseTextHologramLine implements TextHologramLine, APIClickableHologramLine {

    private final APIHologram parent;

    private ClickListener clickListener;

    public APITextHologramLine(APIHologram parent, String text) {
        super(parent, text);
        this.parent = parent;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return parent.isAllowPlaceholders();
    }

    @Override
    public void setClickListener(@Nullable ClickListener clickListener) {
        checkNotDeleted();

        this.clickListener = clickListener;
    }

    @Override
    public @Nullable ClickListener getClickListener() {
        return clickListener;
    }

}
