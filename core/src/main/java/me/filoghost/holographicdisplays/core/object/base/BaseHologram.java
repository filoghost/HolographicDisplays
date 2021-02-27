/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.object.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public abstract class BaseHologram extends HologramComponent {
    
    private final NMSManager nmsManager;
    
    private boolean deleted;

    public BaseHologram(Location location, NMSManager nmsManager) {
        Preconditions.notNull(location, "location");
        this.setLocation(location);
        this.nmsManager = nmsManager;
    }

    public abstract Plugin getOwner();

    public abstract List<? extends SpawnableHologramLine> getLinesUnsafe();
    
    public abstract boolean isAllowPlaceholders();

    public abstract boolean isVisibleTo(Player player);

    protected abstract double getSpaceBetweenLines();
    
    public abstract String toFormattedString();
    
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        if (!deleted) {
            deleted = true;
            clearLines();
        }
    }

    public NMSManager getNMSManager() {
        return nmsManager;
    }
    
    public void removeLine(int index) {
        checkState();

        getLinesUnsafe().remove(index).despawn();
        refresh();
    }

    public void removeLine(BaseHologramLine line) {
        checkState();

        getLinesUnsafe().remove(line);
        line.despawn();
        refresh();
    }
    
    public void clearLines() {
        for (SpawnableHologramLine line : getLinesUnsafe()) {
            line.despawn();
        }
        
        getLinesUnsafe().clear();
    }
    
    public int size() {
        return getLinesUnsafe().size();
    }
    
    public void teleport(Location location) {
        Preconditions.notNull(location, "location");
        teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }
    
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
                currentLineY -= getSpaceBetweenLines();
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
