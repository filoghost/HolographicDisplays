/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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

    public ChunkListener(Plugin plugin, InternalHologramManager internalHologramManager, APIHologramManager apiHologramManager) {
        this.plugin = plugin;
        this.internalHologramManager = internalHologramManager;
        this.apiHologramManager = apiHologramManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldLoad(WorldUnloadEvent event) {
        internalHologramManager.onWorldLoad(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        internalHologramManager.onWorldUnload(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        internalHologramManager.onChunkUnload(event.getChunk());
        apiHologramManager.onChunkUnload(event.getChunk());
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
    }

}
