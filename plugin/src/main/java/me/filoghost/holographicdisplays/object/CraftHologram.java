/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.object.line.CraftItemLine;
import me.filoghost.holographicdisplays.object.line.CraftTextLine;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is only used by the plugin itself. Do not attempt to use it.
 */
public class CraftHologram implements Hologram {
    
    // Position variables.
    private World world;
    private double x, y, z;
    private int chunkX, chunkZ;
    
    // The entities that represent lines.
    private final List<CraftHologramLine> lines;
    
    private CraftVisibilityManager visibilityManager;
    private boolean allowPlaceholders;
    private long creationTimestamp;
    private boolean deleted;
    
    public CraftHologram(Location location) {
        Preconditions.notNull(location, "location");
        updateLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
        
        lines = new ArrayList<>();
        allowPlaceholders = false;
        creationTimestamp = System.currentTimeMillis();
        visibilityManager = new CraftVisibilityManager(this);
    }
    
    public boolean isInChunk(Chunk chunk) {
        return chunk.getWorld() == world && chunk.getX() == chunkX && chunk.getZ() == chunkZ;
    }
    
    @Override
    public World getWorld() {
        return world;
    }
    
    @Override
    public double getX() {
        return x;
    }
    
    @Override
    public double getY() {
        return y;
    }
    
    @Override
    public double getZ() {
        return z;
    }
    
    @Override
    public Location getLocation() {
        return new Location(world, x, y, z);
    }
    
    private void updateLocation(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        chunkX = Utils.floor(x) >> 4;
        chunkZ = Utils.floor(z) >> 4;
    }
    
    @Override
    public boolean isDeleted() {
        return deleted;
    }
    
    @Override
    public void delete() {
        if (!deleted) {
            deleted = true;
            clearLines();
        }
    }
    
    public List<CraftHologramLine> getLinesUnsafe() {
        return lines;
    }
    
    @Override
    public CraftHologramLine getLine(int index) {
        return lines.get(index);
    }
    
    @Override
    public CraftTextLine appendTextLine(String text) {
        Preconditions.checkState(!deleted, "hologram already deleted");
        
        CraftTextLine line = new CraftTextLine(this, text);
        lines.add(line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public CraftItemLine appendItemLine(ItemStack itemStack) {
        Preconditions.checkState(!deleted, "hologram already deleted");
        Preconditions.notNull(itemStack, "itemStack");
        
        CraftItemLine line = new CraftItemLine(this, itemStack);
        lines.add(line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public CraftTextLine insertTextLine(int index, String text) {
        Preconditions.checkState(!deleted, "hologram already deleted");
        
        CraftTextLine line = new CraftTextLine(this, text);
        lines.add(index, line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public CraftItemLine insertItemLine(int index, ItemStack itemStack) {
        Preconditions.checkState(!deleted, "hologram already deleted");
        Preconditions.notNull(itemStack, "itemStack");
        
        CraftItemLine line = new CraftItemLine(this, itemStack);
        lines.add(index, line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public void removeLine(int index) {
        Preconditions.checkState(!deleted, "hologram already deleted");
        
        lines.remove(index).despawn();
        refreshSingleLines();
    }
    
    public void removeLine(CraftHologramLine line) {
        Preconditions.checkState(!deleted, "hologram already deleted");
        
        lines.remove(line);
        line.despawn();
        refreshSingleLines();
    }
    
    @Override
    public void clearLines() {
        for (CraftHologramLine line : lines) {
            line.despawn();
        }
        
        lines.clear();
    }
    
    @Override
    public int size() {
        return lines.size();
    }
    
    @Override
    public double getHeight() {
        if (lines.isEmpty()) {
            return 0;
        }
        
        double height = 0.0;
        
        for (CraftHologramLine line : lines) {
            height += line.getHeight();
        }
        
        height += Configuration.spaceBetweenLines * (lines.size() - 1);
        return height;
    }
    
    @Override
    public CraftVisibilityManager getVisibilityManager() {
        return visibilityManager;
    }
    
    
    @Override
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    @Override
    public void setAllowPlaceholders(boolean allowPlaceholders) {
        if (this.allowPlaceholders == allowPlaceholders) {
            return;
        }
        
        this.allowPlaceholders = allowPlaceholders;
        refreshAll();
    }
    

    public void refreshAll() {
        if (world.isChunkLoaded(chunkX, chunkZ)) {
            spawnEntities();
        }
    }
    
    public void refreshSingleLines() {
        if (world.isChunkLoaded(chunkX, chunkZ)) {
            
            double currentY = this.y;
            boolean first = true;
            
            for (CraftHologramLine line : lines) {
                
                currentY -= line.getHeight();
                
                if (first) {
                    first = false;
                } else {
                    currentY -= Configuration.spaceBetweenLines;
                }
                
                if (line.isSpawned()) {
                    line.teleport(x, currentY, z);
                } else {
                    line.spawn(world, x, currentY, z);
                    if (allowPlaceholders && line instanceof CraftTextLine) {
                        PlaceholdersManager.trackIfNecessary((CraftTextLine) line);
                    }
                }
            }
        }
    }
    
    /**
     * Forces the entities to spawn, without checking if the chunk is loaded.
     */
    public void spawnEntities() {
        Preconditions.checkState(!deleted, "hologram already deleted");
        
        despawnEntities();

        double currentY = this.y;
        boolean first = true;
        
        for (CraftHologramLine line : lines) {
            
            currentY -= line.getHeight();
            
            if (first) {
                first = false;
            } else {
                currentY -= Configuration.spaceBetweenLines;
            }
            
            line.spawn(world, x, currentY, z);
            if (allowPlaceholders && line instanceof CraftTextLine) {
                PlaceholdersManager.trackIfNecessary((CraftTextLine) line);
            }
        }
    }
    
    /**
     * Called by the PluginHologramManager when the chunk is unloaded.
     */
    public void despawnEntities() {
        for (CraftHologramLine piece : lines) {
            piece.despawn();
        }
    }
    
    @Override
    public void teleport(Location location) {
        Preconditions.notNull(location, "location");
        teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }
    
    @Override
    public void teleport(World world, double x, double y, double z) {
        Preconditions.checkState(!deleted, "hologram already deleted");
        Preconditions.notNull(world, "world");
        
        updateLocation(world, x, y, z);
        
        if (this.world != world) {
            despawnEntities();
            refreshAll();
            return;
        }
        
        double currentY = y;
        boolean first = true;
        
        for (CraftHologramLine line : lines) {
            
            if (!line.isSpawned()) {
                continue;
            }
            
            currentY -= line.getHeight();
            
            if (first) {
                first = false;
            } else {
                currentY -= Configuration.spaceBetweenLines;
            }
            
            line.teleport(x, currentY, z);
        }
    }

    @Override
    public String toString() {
        return "CraftHologram [world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", lines=" + lines + ", deleted=" + deleted + "]";
    }

    /*
     * About equals() and hashcode()
     * Two holograms can never be equal. Even if they have the same position and the same elements, they are still two different objects.
     * The equals and hashcode methods are not overridden, two holograms are equal only if they have the same memory address.
     */
    
}
