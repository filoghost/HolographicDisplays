/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTracker;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class BaseHologramLine extends BaseHologramComponent implements EditableHologramLine {

    private final BaseHologram<?> hologram;
    private final LineTracker<?> tracker;

    protected BaseHologramLine(BaseHologram<?> hologram) {
        Preconditions.notNull(hologram, "parent hologram");
        this.hologram = hologram;
        this.tracker = createTracker(hologram.getTrackerManager());
    }

    protected abstract LineTracker<?> createTracker(LineTrackerManager trackerManager);

    public final void setChanged() {
        tracker.setLineChanged();
    }

    protected final boolean isTrackedPlayer(Player player) {
        return tracker.isTrackedPlayer(player);
    }

    @Override
    public final void setLocation(World world, double x, double y, double z) {
        super.setLocation(world, x, y, z);
        setChanged();
    }

    public final boolean isVisibleTo(Player player) {
        return hologram.isVisibleTo(player);
    }

    public final Plugin getCreatorPlugin() {
        return hologram.getCreatorPlugin();
    }

}
