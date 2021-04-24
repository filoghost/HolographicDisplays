/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.base;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.core.DebugLogger;
import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.SpawnFailedException;
import me.filoghost.holographicdisplays.placeholder.PlaceholderManager;
import org.bukkit.World;

public abstract class BaseHologramLine extends BaseHologramComponent implements StandardHologramLine {
    
    private final BaseHologram<?> hologram;

    private boolean isSpawned;

    protected BaseHologramLine(BaseHologram<?> hologram) {
        Preconditions.notNull(hologram, "parent hologram");
        this.hologram = hologram;
    }
    
    @Override
    public final StandardHologram getHologram() {
        return hologram;
    }

    protected final NMSManager getNMSManager() {
        return hologram.getNMSManager();
    }

    protected final PlaceholderManager getPlaceholderManager() {
        return hologram.getPlaceholderManager();
    }

    @Override
    public final void respawn(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        
        boolean changedWorld = world != getWorld();
        setLocation(world, x, y, z);
        
        try {
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
        } catch (SpawnFailedException e) {
            DebugLogger.handleSpawnFail(e, this);
        }

        isSpawned = true;
    }

    @Override
    public final void despawn() {
        despawnEntities();
        isSpawned = false;
    }
    
    protected final boolean isSpawned() {
        return isSpawned;
    }

    protected abstract void spawnEntities(World world, double x, double y, double z) throws SpawnFailedException;

    protected abstract void teleportEntities(double x, double y, double z);

    protected abstract void despawnEntities();

}
