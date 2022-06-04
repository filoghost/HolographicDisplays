/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.core.api.current.DefaultVisibilitySettings;
import me.filoghost.holographicdisplays.core.tracking.LineTracker;
import me.filoghost.holographicdisplays.core.tracking.LineTrackerManager;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseHologramLine extends BaseHologramComponent implements EditableHologramLine {

    private final BaseHologram hologram;
    private final LineTracker<?> tracker;

    private PositionCoordinates coordinates;

    protected BaseHologramLine(BaseHologram hologram) {
        Preconditions.notNull(hologram, "hologram");
        this.hologram = hologram;
        this.tracker = createTracker(hologram.getTrackerManager());
    }

    protected abstract LineTracker<?> createTracker(LineTrackerManager trackerManager);

    public final void setChanged() {
        tracker.setLineChanged();
    }

    protected final boolean isViewer(Player player) {
        return tracker.isViewer(player);
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

    protected boolean canInteract(Player player) {
        return !isDeleted()
                && player.isOnline()
                && player.getGameMode() != GameMode.SPECTATOR
                && isViewer(player)
                && isVisibleTo(player);
    }

}
