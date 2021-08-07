/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.util.CachedBoolean;
import org.bukkit.Bukkit;
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
import java.util.Objects;

public abstract class BaseHologram<T extends EditableHologramLine> extends BaseHologramComponent {

    private final List<T> lines;
    private final List<T> unmodifiableLinesView;
    private final LineTrackerManager lineTrackerManager;

    private @Nullable World world;
    private String worldName;
    private double x, y, z;
    private int chunkX, chunkZ;
    private final CachedBoolean isInLoadedChunk = new CachedBoolean(() -> world != null && world.isChunkLoaded(chunkX, chunkZ));

    public BaseHologram(BaseHologramPosition position, LineTrackerManager lineTrackerManager) {
        this.lines = new ArrayList<>();
        this.unmodifiableLinesView = Collections.unmodifiableList(lines);
        this.lineTrackerManager = lineTrackerManager;
        setPosition(position);
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

    public int getLineCount() {
        return lines.size();
    }

    public BaseHologramPosition getBasePosition() {
        return new BaseHologramPosition(getPositionWorldName(), getPositionX(), getPositionY(), getPositionZ());
    }

    public @Nullable World getPositionWorldIfLoaded() {
        return world;
    }

    public @NotNull String getPositionWorldName() {
        return worldName;
    }

    public double getPositionX() {
        return x;
    }

    public double getPositionY() {
        return y;
    }

    public double getPositionZ() {
        return z;
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

        this.x = x;
        this.y = y;
        this.z = z;

        int chunkX = getChunkCoordinate(x);
        int chunkZ = getChunkCoordinate(z);
        if (!Objects.equals(this.worldName, worldName) || this.chunkX != chunkX || this.chunkZ != chunkZ) {
            this.world = Bukkit.getWorld(worldName);
            this.worldName = worldName;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.isInLoadedChunk.invalidate();
        }

        updateLineLocations();
    }

    private int getChunkCoordinate(double positionCoordinate) {
        return Location.locToBlock(positionCoordinate) >> 4;
    }

    /**
     * When spawning at a location, the top part of the first line should be exactly on that location.
     * The second line is below the first, and so on.
     */
    private void updateLineLocations() {
        double currentLineY = y;

        for (int i = 0; i < lines.size(); i++) {
            T line = lines.get(i);

            currentLineY -= line.getHeight();
            if (i > 0) {
                currentLineY -= Settings.spaceBetweenLines;
            }

            line.setLocation(x, currentLineY, z);
        }
    }

    protected void onWorldLoad(World world) {
        if (BaseHologramPosition.isSameWorld(world, this.worldName)) {
            this.world = world;
            isInLoadedChunk.invalidate();
        }
    }

    protected void onWorldUnload(World world) {
        if (BaseHologramPosition.isSameWorld(world, this.worldName)) {
            this.world = null;
            isInLoadedChunk.set(false);
        }
    }

    protected void onChunkLoad(Chunk chunk) {
        if (world == chunk.getWorld() && chunkX == chunk.getX() && chunkZ == chunk.getZ()) {
            isInLoadedChunk.set(true);
        }
    }

    protected void onChunkUnload(Chunk chunk) {
        if (world == chunk.getWorld() && chunkX == chunk.getX() && chunkZ == chunk.getZ()) {
            isInLoadedChunk.set(false);
        }
    }

    protected boolean isInLoadedChunk() {
        return isInLoadedChunk.get();
    }

    @Override
    public String toString() {
        return "Hologram{"
                + "position=" + getBasePosition()
                + ", lines=" + lines
                + ", deleted=" + isDeleted()
                + "}";
    }

}
