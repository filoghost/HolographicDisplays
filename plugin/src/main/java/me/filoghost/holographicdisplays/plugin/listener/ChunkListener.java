/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

public class ChunkListener implements Listener {

    private final Plugin plugin;
    private final InternalHologramManager internalHologramManager;
    private final APIHologramManager apiHologramManager;
    private final V2HologramManager v2HologramManager;

    public ChunkListener(
            Plugin plugin,
            InternalHologramManager internalHologramManager,
            APIHologramManager apiHologramManager,
            V2HologramManager v2HologramManager) {
        this.plugin = plugin;
        this.internalHologramManager = internalHologramManager;
        this.apiHologramManager = apiHologramManager;
        this.v2HologramManager = v2HologramManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        internalHologramManager.onWorldUnload(world);
        apiHologramManager.onWorldUnload(world);
        v2HologramManager.onWorldUnload(world);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        internalHologramManager.onWorldLoad(world);
        apiHologramManager.onWorldLoad(world);
        v2HologramManager.onWorldLoad(world);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        internalHologramManager.onChunkUnload(chunk);
        apiHologramManager.onChunkUnload(chunk);
        v2HologramManager.onChunkUnload(chunk);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        // Other plugins could call this event wrongly, check if the chunk is actually loaded
        if (!chunk.isLoaded()) {
            return;
        }

        // In case another plugin loads the chunk asynchronously, always make sure to load the holograms on the main thread
        if (Bukkit.isPrimaryThread()) {
            onChunkLoad(chunk);
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> onChunkLoad(chunk));
        }
    }

    private void onChunkLoad(Chunk chunk) {
        internalHologramManager.onChunkLoad(chunk);
        apiHologramManager.onChunkLoad(chunk);
        v2HologramManager.onChunkLoad(chunk);
    }

}
