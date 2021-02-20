/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.object.BaseHologram;
import org.bukkit.World;

public abstract class HologramLineImpl implements HologramLine {
    
    private final BaseHologram parent;
    
    // This field is necessary for teleport.
    private boolean isSpawned;
    
    // Useful for saving to disk.
    private String serializedConfigValue;
    
    protected HologramLineImpl(BaseHologram parent) {
        Preconditions.notNull(parent, "parent hologram");
        this.parent = parent;
    }
    
    @Override
    public final BaseHologram getParent() {
        return parent;
    }
    
    protected final NMSManager getNMSManager() {
        return parent.getNMSManager();
    }
    
    @Override
    public void removeLine() {
        parent.removeLine(this);
    }

    public void spawn(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        
        // Remove the old entities when spawning the new ones.
        despawn();
        isSpawned = true;
        
        spawnEntities(world, x, y, z);
    }
    
    public final void despawn() {
        isSpawned = false;
        despawnEntities();
    }

    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

    public void setSerializedConfigValue(String serializedConfigValue) {
        this.serializedConfigValue = serializedConfigValue;
    }

    public final boolean isSpawned() {
        return isSpawned;
    }

    protected abstract void spawnEntities(World world, double x, double y, double z);

    protected abstract void despawnEntities();

    public abstract void teleport(double x, double y, double z);

    public abstract double getHeight();
    
    public abstract int[] getEntitiesIDs();

}
