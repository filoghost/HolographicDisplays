/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.api.line.ItemLine;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.Configuration;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.line.HologramLineImpl;
import me.filoghost.holographicdisplays.object.line.ItemLineImpl;
import me.filoghost.holographicdisplays.object.line.TextLineImpl;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseHologram implements Hologram {
    
    private final NMSManager nmsManager;
    
    // Position variables.
    private World world;
    private double x, y, z;
    private int chunkX, chunkZ;
    
    // The entities that represent lines.
    private final List<HologramLineImpl> lines;
    
    private final VisibilityManager visibilityManager;
    private boolean allowPlaceholders;
    private final long creationTimestamp;
    private boolean deleted;
    
    public BaseHologram(Location location, NMSManager nmsManager) {
        Preconditions.notNull(location, "location");
        setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());

        this.nmsManager = nmsManager;
        this.lines = new ArrayList<>();
        this.creationTimestamp = System.currentTimeMillis();
        this.visibilityManager = new VisibilityManagerImpl(this);
        
        this.allowPlaceholders = false;
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
    
    private void setLocation(World world, double x, double y, double z) {
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
    
    public void setDeleted() {
        if (!deleted) {
            deleted = true;
            clearLines();
        }
    }
    
    public List<HologramLineImpl> getLinesUnsafe() {
        return lines;
    }
    
    @Override
    public HologramLine getLine(int index) {
        return lines.get(index);
    }
    
    @Override
    public TextLine appendTextLine(String text) {
        checkState();

        TextLineImpl line = new TextLineImpl(this, text);
        lines.add(line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public ItemLine appendItemLine(ItemStack itemStack) {
        checkState();
        Preconditions.notNull(itemStack, "itemStack");
        
        ItemLineImpl line = new ItemLineImpl(this, itemStack);
        lines.add(line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public TextLine insertTextLine(int index, String text) {
        checkState();

        TextLineImpl line = new TextLineImpl(this, text);
        lines.add(index, line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public ItemLine insertItemLine(int index, ItemStack itemStack) {
        checkState();
        Preconditions.notNull(itemStack, "itemStack");
        
        ItemLineImpl line = new ItemLineImpl(this, itemStack);
        lines.add(index, line);
        refreshSingleLines();
        return line;
    }
    
    @Override
    public void removeLine(int index) {
        checkState();

        lines.remove(index).despawn();
        refreshSingleLines();
    }
    
    public void removeLine(HologramLineImpl line) {
        checkState();

        lines.remove(line);
        line.despawn();
        refreshSingleLines();
    }
    
    @Override
    public void clearLines() {
        for (HologramLineImpl line : lines) {
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
        
        for (HologramLineImpl line : lines) {
            height += line.getHeight();
        }
        
        height += Configuration.spaceBetweenLines * (lines.size() - 1);
        return height;
    }
    
    @Override
    public VisibilityManager getVisibilityManager() {
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

    public NMSManager getNMSManager() {
        return nmsManager;
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
            
            for (HologramLineImpl line : lines) {
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
                    if (allowPlaceholders && line instanceof TextLineImpl) {
                        PlaceholdersManager.trackIfNecessary((TextLineImpl) line);
                    }
                }
            }
        }
    }
    
    /**
     * Forces the entities to (re)spawn, without checking if the chunk is loaded.
     */
    public void spawnEntities() {
        checkState();

        despawnEntities();

        double currentY = this.y;
        boolean first = true;
        
        for (HologramLineImpl line : lines) {
            currentY -= line.getHeight();
            
            if (first) {
                first = false;
            } else {
                currentY -= Configuration.spaceBetweenLines;
            }
            
            line.spawn(world, x, currentY, z);
            if (allowPlaceholders && line instanceof TextLineImpl) {
                PlaceholdersManager.trackIfNecessary((TextLineImpl) line);
            }
        }
    }
    
    public void despawnEntities() {
        for (HologramLineImpl piece : lines) {
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
        checkState();
        Preconditions.notNull(world, "world");
        
        setLocation(world, x, y, z);
        
        if (this.world != world) {
            despawnEntities();
            refreshAll();
            return;
        }
        
        double currentY = y;
        boolean first = true;
        
        for (HologramLineImpl line : lines) {
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

    private void checkState() {
        Preconditions.checkState(!deleted, "hologram already deleted");
    }

    @Override
    public String toString() {
        return "BaseHologram [world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", lines=" + lines + ", deleted=" + deleted + "]";
    }

    /*
     * Object.equals() and Object.hashCode() are not overridden:
     * two holograms are equal only if they are the same exact instance.
     */
    
}
