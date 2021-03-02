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
import org.bukkit.World;

public abstract class BaseHologramLine extends BaseHologramComponent implements StandardHologramLine {
    
    private final StandardHologram parent;
    
    private boolean isSpawned;

    protected BaseHologramLine(StandardHologram parent) {
        Preconditions.notNull(parent, "parent hologram");
        this.parent = parent;
    }
    
    @Override
    public final StandardHologram getHologram() {
        return parent;
    }

    protected final NMSManager getNMSManager() {
        return parent.getNMSManager();
    }

    @Override
    public final void respawn(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        
        boolean changedWorld = world != getWorld();
        setLocation(world, x, y, z);
        
        if (changedWorld) {
            // World has changed, entities must be fully respawned
            despawnEntities();
            spawnEntities(world, x, y, z);
            
        } else if (isSpawned) {
            // Line is already spawned, respawn can be avoided
            teleportEntities(x, y, z);
            
        } else {
            // Line is not spawned, entities must be spawned
            spawnEntities(world, x, y, z);
        }

        isSpawned = true;
    }

    @Override
    public final void despawn() {
        despawnEntities();
        isSpawned = false;
    }
    
    protected boolean isSpawned() {
        return isSpawned;
    }

    protected abstract void spawnEntities(World world, double x, double y, double z);

    protected abstract void teleportEntities(double x, double y, double z);

    protected abstract void despawnEntities();

}
