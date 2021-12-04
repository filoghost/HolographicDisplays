/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.holographicdisplays.api.beta.hologram.ResolvePlaceholders;
import me.filoghost.holographicdisplays.api.beta.hologram.line.HologramLineClickListener;
import me.filoghost.holographicdisplays.api.beta.hologram.line.TextHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseTextHologramLine;
import org.jetbrains.annotations.Nullable;

class APITextHologramLine extends BaseTextHologramLine implements TextHologramLine, APIClickableHologramLine {

    private final APIHologram hologram;

    private HologramLineClickListener clickListener;

    APITextHologramLine(APIHologram hologram, String text) {
        super(hologram, text);
        this.hologram = hologram;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return hologram.getResolvePlaceholders() == ResolvePlaceholders.ALL;
    }

    @Override
    public void setClickListener(@Nullable HologramLineClickListener clickListener) {
        checkNotDeleted();

        this.clickListener = clickListener;
    }

    @Override
    public @Nullable HologramLineClickListener getClickListener() {
        return clickListener;
    }

}
