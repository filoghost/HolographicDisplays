/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.fcommons.Preconditions;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HologramManager<H extends BaseHologram> {

    private final List<H> holograms = new ArrayList<>();
    private final List<H> unmodifiableHologramsView = Collections.unmodifiableList(holograms);

    protected void addHologram(H hologram) {
        holograms.add(hologram);
    }

    protected void deleteHologram(H hologram) {
        Preconditions.checkArgument(hologram.isDeleted(), "hologram must be deleted first");
        holograms.remove(hologram);
        hologram.setDeleted();
    }

    public List<H> getHolograms() {
        return unmodifiableHologramsView;
    }

    public void clearAll() {
        List<H> oldHolograms = new ArrayList<>(holograms);
        holograms.clear();

        for (H hologram : oldHolograms) {
            hologram.delete();
        }
    }
    
    public void onChunkLoad(Chunk chunk) {
        // Load the holograms in that chunk.
        for (H hologram : holograms) {
            if (hologram.isInChunk(chunk)) {
                hologram.spawnEntities();
            }
        }
    }

}
