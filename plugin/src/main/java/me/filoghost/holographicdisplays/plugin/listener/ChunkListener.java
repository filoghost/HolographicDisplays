/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.common.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    private final NMSManager nmsManager;
    private final InternalHologramManager internalHologramManager;
    private final APIHologramManager apiHologramManager;

    public ChunkListener(NMSManager nmsManager, InternalHologramManager internalHologramManager, APIHologramManager apiHologramManager) {
        this.nmsManager = nmsManager;
        this.internalHologramManager = internalHologramManager;
        this.apiHologramManager = apiHologramManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (!entity.isDead()) {
                NMSEntity entityBase = nmsManager.getNMSEntityBase(entity);

                if (entityBase != null) {
                    entityBase.getHologramLine().getHologram().despawnEntities();
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
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
            Bukkit.getScheduler().runTask(HolographicDisplays.getInstance(), () -> onChunkLoad(chunk));
        }
    }

    private void onChunkLoad(Chunk chunk) {
        internalHologramManager.onChunkLoad(chunk);
        apiHologramManager.onChunkLoad(chunk);
    }

}
