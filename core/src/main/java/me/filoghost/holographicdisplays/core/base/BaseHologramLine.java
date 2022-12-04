/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.api.current.DefaultVisibilitySettings;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseHologramLine extends BaseHologramComponent implements EditableHologramLine {

    private final BaseHologram hologram;

    private PositionCoordinates coordinates;

    /**
     * Flag to indicate that the line has changed in some way and update packets might be necessary.
     */
    private boolean changed;

    protected BaseHologramLine(BaseHologram hologram) {
        Preconditions.notNull(hologram, "hologram");
        this.hologram = hologram;
        setChanged(); // Force the initial refresh
    }

    public boolean hasChanged() {
        return changed;
    }

    public final void setChanged() {
        changed = true;
    }

    public void clearChanged() {
        changed = false;
    }

    @Override
    public final void setCoordinates(double x, double y, double z) {
        coordinates = new PositionCoordinates(x, y, z);
        setChanged();
    }

    public @NotNull PositionCoordinates getCoordinates() {
        if (coordinates == null) {
            throw new IllegalStateException("position not set");
        }
        return coordinates;
    }

    public @NotNull String getWorldName() {
        return hologram.getPosition().getWorldName();
    }

    public @Nullable World getWorldIfLoaded() {
        return hologram.getWorldIfLoaded();
    }

    public boolean isInLoadedChunk() {
        return hologram.isInLoadedChunk();
    }

    public final boolean isVisibleTo(Player player) {
        return hologram.getVisibilitySettings().isVisibleTo(player);
    }

    public final Plugin getCreatorPlugin() {
        return hologram.getCreatorPlugin();
    }

    public final DefaultVisibilitySettings getVisibilitySettings() {
        return hologram.getVisibilitySettings();
    }

}
