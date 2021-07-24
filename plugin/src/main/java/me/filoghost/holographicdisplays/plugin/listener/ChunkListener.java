/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

public class ChunkListener implements Listener {

    private final Plugin plugin;
    private final LineTrackerManager lineTrackerManager;

    public ChunkListener(Plugin plugin, LineTrackerManager lineTrackerManager) {
        this.plugin = plugin;
        this.lineTrackerManager = lineTrackerManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        lineTrackerManager.onChunkUnload(event.getChunk());
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
        lineTrackerManager.onChunkLoad(chunk);
    }

}
