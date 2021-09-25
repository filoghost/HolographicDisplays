/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.hologram.ResolvePlaceholders;
import me.filoghost.holographicdisplays.api.hologram.line.ClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;
import org.jetbrains.annotations.Nullable;

class APITextHologramLine extends BaseTextHologramLine implements TextHologramLine, APIClickableHologramLine {

    private final APIHologram hologram;

    private ClickListener clickListener;

    APITextHologramLine(APIHologram hologram, String text) {
        super(hologram, text);
        this.hologram = hologram;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return hologram.getResolvePlaceholders() == ResolvePlaceholders.ALL;
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
