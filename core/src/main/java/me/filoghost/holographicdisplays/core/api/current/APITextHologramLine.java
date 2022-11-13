/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.holographicdisplays.api.hologram.PlaceholderSetting;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLineClickListener;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import me.filoghost.holographicdisplays.core.CorePreconditions;
import me.filoghost.holographicdisplays.core.base.BaseTextHologramLine;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

class APITextHologramLine extends BaseTextHologramLine implements TextHologramLine, APIHologramLine {

    private final APIHologram hologram;

    private HologramLineClickListener clickListener;

    APITextHologramLine(APIHologram hologram, String text) {
        super(hologram, text);
        this.hologram = hologram;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return hologram.getPlaceholderSetting() == PlaceholderSetting.ENABLE_ALL;
    }

    @Override
    public void setClickListener(@Nullable HologramLineClickListener clickListener) {
        CorePreconditions.checkMainThread();
        checkNotDeleted();

        this.clickListener = clickListener;
        setChanged();
    }

    @Override
    public @Nullable HologramLineClickListener getClickListener() {
        return clickListener;
    }

    @Override
    public boolean hasClickCallback() {
        return clickListener != null;
    }

    @Override
    protected void invokeClickCallback(Player player) {
        if (clickListener != null) {
            clickListener.onClick(new SimpleHologramLineClickEvent(player));
        }
    }

}
