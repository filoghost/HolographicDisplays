/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseHologram<T extends EditableHologramLine> extends BaseHologramComponent {

    private final WorldAwareHologramPosition position;
    private final List<T> lines;
    private final List<T> unmodifiableLinesView;
    private final LineTrackerManager lineTrackerManager;

    public BaseHologram(BaseHologramPosition position, LineTrackerManager lineTrackerManager) {
        this.position = new WorldAwareHologramPosition(position);
        this.lines = new ArrayList<>();
        this.unmodifiableLinesView = Collections.unmodifiableList(lines);
        this.lineTrackerManager = lineTrackerManager;
    }

    protected abstract boolean isVisibleTo(Player player);

    public abstract Plugin getCreatorPlugin();

    protected final LineTrackerManager getTrackerManager() {
        return lineTrackerManager;
    }

    public List<T> getLines() {
        return unmodifiableLinesView;
    }

    public void addLine(T line) {
        checkNotDeleted();

        lines.add(line);
        updateLinePositions();
    }

    public void addLines(List<? extends T> newLines) {
        checkNotDeleted();

        lines.addAll(newLines);
        updateLinePositions();
    }

    public void insertLine(int afterIndex, T line) {
        checkNotDeleted();

        lines.add(afterIndex, line);
        updateLinePositions();
    }

    public void setLine(int index, T line) {
        checkNotDeleted();

        T previousLine = lines.set(index, line);
        previousLine.setDeleted();
        updateLinePositions();
    }

    public void setLines(List<T> newLines) {
        checkNotDeleted();

        clearLines();
        lines.addAll(newLines);
        updateLinePositions();
    }

    public void removeLine(int index) {
        checkNotDeleted();

        lines.remove(index).setDeleted();
        updateLinePositions();
    }

    public void removeLine(T line) {
        checkNotDeleted();

        lines.remove(line);
        line.setDeleted();
        updateLinePositions();
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

    public int getLineCount() {
        return lines.size();
    }

    public BaseHologramPosition getBasePosition() {
        return position.toBasePosition();
    }

    public @Nullable World getPositionWorldIfLoaded() {
        return position.getWorldIfLoaded();
    }

    public @NotNull String getPositionWorldName() {
        return position.getWorldName();
    }

    public double getPositionX() {
        return position.getX();
    }

    public double getPositionY() {
        return position.getY();
    }

    public double getPositionZ() {
        return position.getZ();
    }

    public void setPosition(@NotNull BaseHologramPosition position) {
        Preconditions.notNull(position, "position");
        setPosition(position.getWorldName(), position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        Preconditions.notNull(location.getWorld(), "location's world");
        setPosition(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    public void setPosition(@NotNull World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        setPosition(world.getName(), x, y, z);
    }

    public void setPosition(@NotNull String worldName, double x, double y, double z) {
        Preconditions.notNull(worldName, "worldName");
        checkNotDeleted();

        position.set(worldName, x, y, z);
        updateLinePositions();
    }

    /**
     * When spawning at a location, the top part of the first line should be exactly on that location.
     * The second line is below the first, and so on.
     */
    private void updateLinePositions() {
        double currentLineY = position.getY();

        for (int i = 0; i < lines.size(); i++) {
            T line = lines.get(i);

            currentLineY -= line.getHeight();
            if (i > 0) {
                currentLineY -= Settings.spaceBetweenLines;
            }

            line.setPosition(position.getX(), currentLineY, position.getZ());
        }
    }

    protected void onWorldLoad(World world) {
        position.onWorldLoad(world);
    }

    protected void onWorldUnload(World world) {
        position.onWorldUnload(world);
    }

    protected void onChunkLoad(Chunk chunk) {
        position.onChunkLoad(chunk);
    }

    protected void onChunkUnload(Chunk chunk) {
        position.onChunkUnload(chunk);
    }

    protected boolean isInLoadedChunk() {
        return position.isChunkLoaded();
    }

    @Override
    public String toString() {
        return "Hologram{"
                + "position=" + position
                + ", lines=" + lines
                + ", deleted=" + isDeleted()
                + "}";
    }

}
