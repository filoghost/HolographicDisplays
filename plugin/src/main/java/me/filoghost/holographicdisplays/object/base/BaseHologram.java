/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.placeholder.tracking.PlaceholderLineTracker;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseHologram<T extends StandardHologramLine> extends BaseHologramComponent implements StandardHologram {
    
    private final NMSManager nmsManager;
    private final PlaceholderLineTracker placeholderLineTracker;
    private final List<T> lines;
    private final List<T> unmodifiableLinesView;
    
    private boolean deleted;

    public BaseHologram(Location location, NMSManager nmsManager, PlaceholderLineTracker placeholderLineTracker) {
        this.placeholderLineTracker = placeholderLineTracker;
        Preconditions.notNull(location, "location");
        this.setLocation(location);
        this.nmsManager = nmsManager;
        this.lines = new ArrayList<>();
        this.unmodifiableLinesView = Collections.unmodifiableList(lines);
    }

    protected final NMSManager getNMSManager() {
        return nmsManager;
    }

    protected final PlaceholderLineTracker getPlaceholderLineTracker() {
        return placeholderLineTracker;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }
    
    @Override
    public void setDeleted() {
        if (!deleted) {
            deleted = true;
            despawnEntities();
        }
    }

    @Override
    public List<T> getLines() {
        return unmodifiableLinesView;
    }

    public void addLine(T line) {
        checkNotDeleted();
        
        lines.add(line);
        refresh();
    }

    public void addLines(List<? extends T> newLines) {
        checkNotDeleted();
        
        lines.addAll(newLines);
        refresh();
    }

    public void insertLine(int afterIndex, T line) {
        checkNotDeleted();
        
        lines.add(afterIndex, line);
        refresh();
    }

    public void setLine(int index, T line) {
        checkNotDeleted();
        
        T previousLine = lines.set(index, line);
        previousLine.despawn();
        refresh();
    }

    public void setLines(List<T> newLines) {
        checkNotDeleted();
        
        clearLines();
        lines.addAll(newLines);
        refresh();
    }
    
    public void removeLine(int index) {
        checkNotDeleted();

        lines.remove(index).despawn();
        refresh();
    }
    
    public void removeLine(T line) {
        checkNotDeleted();

        lines.remove(line);
        line.despawn();
        refresh();
    }
    
    public void clearLines() {
        checkNotDeleted();
        
        Iterator<T> iterator = lines.iterator();
        while (iterator.hasNext()) {
            T line = iterator.next();
            iterator.remove();
            line.despawn();
        }
        
        // No need to refresh, since there are no lines
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
        refresh();
    }

    @Override
    public void refresh() {
        refresh(false);
    }

    @Override
    public void refresh(boolean forceRespawn) {
        refresh(forceRespawn, isInLoadedChunk());
    }

    @Override
    public void refresh(boolean forceRespawn, boolean isChunkLoaded) {
        checkNotDeleted();
        
        if (isChunkLoaded) {
            respawnEntities(forceRespawn);
        } else {
            despawnEntities();
        }
    }

    /*
     * When spawning at a location, the top part of the first line should be exactly on that location.
     * The second line is below the first, and so on.
     */
    private void respawnEntities(boolean forceRespawn) {
        double currentLineY = getY();

        for (int i = 0; i < lines.size(); i++) {
            T line = lines.get(i);
            
            currentLineY -= line.getHeight();
            if (i > 0) {
                currentLineY -= Configuration.spaceBetweenLines;
            }

            if (forceRespawn) {
                line.despawn();
            }
            line.respawn(getWorld(), getX(), currentLineY, getZ());
        }
    }

    @Override
    public void despawnEntities() {
        for (T line : lines) {
            line.despawn();
        }
    }

    private void checkNotDeleted() {
        Preconditions.checkState(!deleted, "hologram is not usable after being deleted");
    }

    @Override
    public String toString() {
        return "BaseHologram [location=" + getLocation() + ", lines=" + lines + ", deleted=" + deleted + "]";
    }

    /*
     * Object.equals() and Object.hashCode() are not overridden:
     * two holograms are equal only if they are the same exact instance.
     */
    
}
