/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.holographicdisplays.api.line.HologramLine;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import me.filoghost.holographicdisplays.util.Validator;
import org.bukkit.World;

import java.util.Collection;

public abstract class CraftHologramLine implements HologramLine {
    
    private final double height;
    private final CraftHologram parent;
    
    // This field is necessary for teleport.
    private boolean isSpawned;
    
    // Useful for saving to disk.
    private String serializedConfigValue;
    
    protected CraftHologramLine(double height, CraftHologram parent) {
        Validator.notNull(parent, "parent hologram");
        this.height = height;
        this.parent = parent;
    }
    
    public final double getHeight() {
        return height;
    }

    @Override
    public final CraftHologram getParent() {
        return parent;
    }
    
    public void removeLine() {
        parent.removeLine(this);
    }

    public void spawn(World world, double x, double y, double z) {
        Validator.notNull(world, "world");
        
        // Remove the old entities when spawning the new ones.
        despawn();
        isSpawned = true;
        
        // Do nothing, there are no entities in this class.
    }
    
    public void despawn() {
        isSpawned = false;
    }
    
    public final boolean isSpawned() {
        return isSpawned;
    }
    
    public String getSerializedConfigValue() {
        return serializedConfigValue;
    }

    public void setSerializedConfigValue(String serializedConfigValue) {
        this.serializedConfigValue = serializedConfigValue;
    }

    public Collection<RelativePlaceholder> getRelativePlaceholders() {
        return null;
    }
    
    public boolean hasRelativePlaceholders() {
        return getRelativePlaceholders() != null && !getRelativePlaceholders().isEmpty();
    }
    
    public abstract int[] getEntitiesIDs();
    
    public abstract void teleport(double x, double y, double z);

}
