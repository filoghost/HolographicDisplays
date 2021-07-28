/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.common.hologram.StandardHologram;
import me.filoghost.holographicdisplays.common.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.plugin.disk.Settings;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseHologram<T extends StandardHologramLine> extends BaseHologramComponent implements StandardHologram {

    private final LineTrackerManager lineTrackerManager;
    private final List<T> lines;
    private final List<T> unmodifiableLinesView;

    public BaseHologram(Location location, LineTrackerManager lineTrackerManager) {
        Preconditions.notNull(location, "location");
        this.setLocation(location);
        this.lineTrackerManager = lineTrackerManager;
        this.lines = new ArrayList<>();
        this.unmodifiableLinesView = Collections.unmodifiableList(lines);
    }

    protected final LineTrackerManager getTrackerManager() {
        return lineTrackerManager;
    }

    @Override
    public List<T> getLines() {
        return unmodifiableLinesView;
    }

    public void addLine(T line) {
        checkNotDeleted();

        lines.add(line);
        updateLineLocations();
    }

    public void addLines(List<? extends T> newLines) {
        checkNotDeleted();

        lines.addAll(newLines);
        updateLineLocations();
    }

    public void insertLine(int afterIndex, T line) {
        checkNotDeleted();

        lines.add(afterIndex, line);
        updateLineLocations();
    }

    public void setLine(int index, T line) {
        checkNotDeleted();

        T previousLine = lines.set(index, line);
        previousLine.setDeleted();
        updateLineLocations();
    }

    public void setLines(List<T> newLines) {
        checkNotDeleted();

        clearLines();
        lines.addAll(newLines);
        updateLineLocations();
    }

    public void removeLine(int index) {
        checkNotDeleted();

        lines.remove(index).setDeleted();
        updateLineLocations();
    }

    public void removeLine(T line) {
        checkNotDeleted();

        lines.remove(line);
        line.setDeleted();
        updateLineLocations();
    }

    public void clearLines() {
        checkNotDeleted();

        Iterator<T> iterator = lines.iterator();
        while (iterator.hasNext()) {
            T line = iterator.next();
            iterator.remove();
            line.setDeleted();
        }

        // No need to refresh, since there are no lines
    }

    @Override
    public final void setDeleted() {
        super.setDeleted();
        for (T line : lines) {
            line.setDeleted();
        }
    }

    @Override
    public int getLineCount() {
        return lines.size();
    }

    public void teleport(@NotNull Location location) {
        Preconditions.notNull(location, "location");

        teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    public void teleport(@NotNull World world, double x, double y, double z) {
        checkNotDeleted();
        Preconditions.notNull(world, "world");

        setLocation(world, x, y, z);
        updateLineLocations();
    }

    /**
     * When spawning at a location, the top part of the first line should be exactly on that location.
     * The second line is below the first, and so on.
     */
    private void updateLineLocations() {
        double currentLineY = getY();

        for (int i = 0; i < lines.size(); i++) {
            T line = lines.get(i);

            currentLineY -= line.getHeight();
            if (i > 0) {
                currentLineY -= Settings.spaceBetweenLines;
            }

            line.setLocation(getWorld(), getX(), currentLineY, getZ());
        }
    }

    @Override
    public String toString() {
        return "BaseHologram [location=" + getLocation() + ", lines=" + lines + ", deleted=" + isDeleted() + "]";
    }

}
