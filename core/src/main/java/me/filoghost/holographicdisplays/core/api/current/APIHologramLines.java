/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.hologram.HologramLines;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import me.filoghost.holographicdisplays.core.base.BaseHologramLines;
import me.filoghost.holographicdisplays.core.tracking.LineTrackerManager;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class APIHologramLines extends BaseHologramLines<APIHologramLine> implements HologramLines {

    private final APIHologram hologram;

    APIHologramLines(APIHologram hologram, LineTrackerManager lineTrackerManager) {
        super(hologram, lineTrackerManager);
        this.hologram = hologram;
    }

    @Override
    public @NotNull TextHologramLine appendText(@Nullable String text) {
        checkNotDeleted();

        APITextHologramLine line = new APITextHologramLine(hologram, text);
        super.add(line);
        return line;
    }

    @Override
    public @NotNull ItemHologramLine appendItem(@Nullable ItemStack itemStack) {
        checkNotDeleted();

        APIItemHologramLine line = new APIItemHologramLine(hologram, itemStack);
        super.add(line);
        return line;
    }

    @Override
    public @NotNull TextHologramLine insertText(int beforeIndex, @Nullable String text) {
        checkNotDeleted();

        APITextHologramLine line = new APITextHologramLine(hologram, text);
        super.insert(beforeIndex, line);
        return line;
    }

    @Override
    public @NotNull ItemHologramLine insertItem(int beforeIndex, @NotNull ItemStack itemStack) {
        Preconditions.notNull(itemStack, "itemStack");
        checkNotDeleted();

        APIItemHologramLine line = new APIItemHologramLine(hologram, itemStack);
        super.insert(beforeIndex, line);
        return line;
    }

    @Override
    public boolean remove(@NotNull HologramLine line) {
        if (line instanceof APIHologramLine) {
            return super.remove((APIHologramLine) line);
        } else {
            return false;
        }
    }

}
