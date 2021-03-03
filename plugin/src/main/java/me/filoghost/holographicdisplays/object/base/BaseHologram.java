/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.disk.Configuration;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Iterator;

public abstract class BaseHologram extends BaseHologramComponent implements StandardHologram {
    
    private final NMSManager nmsManager;
    
    private boolean deleted;

    public BaseHologram(Location location, NMSManager nmsManager) {
        Preconditions.notNull(location, "location");
        this.setLocation(location);
        this.nmsManager = nmsManager;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted() {
        if (!deleted) {
            deleted = true;
            clearLines();
        }
    }
    
    protected final NMSManager getNMSManager() {
        return nmsManager;
    }
    
    public void removeLine(int index) {
        checkState();

        getLinesUnsafe().remove(index).despawn();
        refresh();
    }

    @Override
    public void removeLine(StandardHologramLine line) {
        checkState();

        getLinesUnsafe().remove(line);
        line.despawn();
        refresh();
    }
    
    public void clearLines() {
        Iterator<? extends StandardHologramLine> iterator = getLinesUnsafe().iterator();
        while (iterator.hasNext()) {
            StandardHologramLine line = iterator.next();
            iterator.remove();
            line.despawn();
        }
    }
    
    @Override
    public int size() {
        return getLinesUnsafe().size();
    }
    
    public void teleport(Location location) {
        checkState();
        Preconditions.notNull(location, "location");

        teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }
    
    public void teleport(World world, double x, double y, double z) {
        checkState();
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
        checkState();
        
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

        for (int i = 0; i < getLinesUnsafe().size(); i++) {
            StandardHologramLine line = getLinesUnsafe().get(i);
            
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
        for (StandardHologramLine line : getLinesUnsafe()) {
            line.despawn();
        }
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
