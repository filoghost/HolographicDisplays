/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.HologramComponent;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public abstract class BaseHologram extends HologramComponent implements Hologram {
    
    private final NMSManager nmsManager;
    private final VisibilityManager visibilityManager;
    private final long creationTimestamp;

    private boolean allowPlaceholders;
    private boolean deleted;

    public BaseHologram(Location location, NMSManager nmsManager) {
        Preconditions.notNull(location, "location");
        this.setLocation(location);
        this.nmsManager = nmsManager;
        this.visibilityManager = new DefaultVisibilityManager(this);
        this.creationTimestamp = System.currentTimeMillis();
        this.allowPlaceholders = false;
    }

    public abstract List<? extends SpawnableHologramLine> getLinesUnsafe();
    
    @Override
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        if (!deleted) {
            deleted = true;
            clearLines();
        }
    }

    @Override
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        if (this.allowPlaceholders == allowPlaceholders) {
            return;
        }

        this.allowPlaceholders = allowPlaceholders;
        refresh(true);
    }
    
    @Override
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    public NMSManager getNMSManager() {
        return nmsManager;
    }

    @Override
    public HologramLine getLine(int index) {
        return getLinesUnsafe().get(index);
    }

    @Override
    public void removeLine(int index) {
        checkState();

        getLinesUnsafe().remove(index).despawn();
        refresh();
    }

    public void removeLine(SpawnableHologramLine line) {
        checkState();

        getLinesUnsafe().remove(line);
        line.despawn();
        refresh();
    }

    @Override
    public void clearLines() {
        for (SpawnableHologramLine line : getLinesUnsafe()) {
            line.despawn();
        }
        
        getLinesUnsafe().clear();
    }

    @Override
    public int size() {
        return getLinesUnsafe().size();
    }

    @Override
    public void teleport(Location location) {
        Preconditions.notNull(location, "location");
        teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void teleport(World world, double x, double y, double z) {
        checkState();
        Preconditions.notNull(world, "world");

        setLocation(world, x, y, z);
        refresh();
    }

    public void refresh() {
        refresh(false);
    }

    public void refresh(boolean forceRespawn) {
        refresh(forceRespawn, isInLoadedChunk());
    }

    public void refresh(boolean forceRespawn, boolean isChunkLoaded) {
        checkState();
        
        if (isChunkLoaded) {
            respawnEntities(forceRespawn);
        } else {
            despawnEntities();
        }
    }

    @Override
    public double getHeight() {
        if (getLinesUnsafe().isEmpty()) {
            return 0;
        }

        double height = 0.0;

        for (SpawnableHologramLine line : getLinesUnsafe()) {
            height += line.getHeight();
        }

        height += Configuration.spaceBetweenLines * (getLinesUnsafe().size() - 1);
        return height;
    }

    /*
     * When spawning at a location, the top part of the first line should be exactly on that location.
     * The second line is below the first, and so on.
     */
    private void respawnEntities(boolean forceRespawn) {
        double currentLineY = getY();

        for (int i = 0; i < getLinesUnsafe().size(); i++) {
            SpawnableHologramLine line = getLinesUnsafe().get(i);
            
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

    public void despawnEntities() {
        for (SpawnableHologramLine line : getLinesUnsafe()) {
            line.despawn();
        }
    }

    @Override
    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

    protected void checkState() {
        Preconditions.checkState(!deleted, "hologram already deleted");
    }

    @Override
    public String toString() {
        return "BaseHologram [location=" + getLocation() + ", lines=" + getLinesUnsafe() + ", deleted=" + deleted + "]";
    }

    /*
     * Object.equals() and Object.hashCode() are not overridden:
     * two holograms are equal only if they are the same exact instance.
     */
    
}
