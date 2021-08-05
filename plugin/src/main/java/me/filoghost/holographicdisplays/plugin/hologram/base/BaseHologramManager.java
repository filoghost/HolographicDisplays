/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseHologramManager<H extends BaseHologram<?>> {

    private final List<H> holograms = new ArrayList<>();
    private final List<H> unmodifiableHologramsView = Collections.unmodifiableList(holograms);

    protected void addHologram(H hologram) {
        holograms.add(hologram);
    }

    public void deleteHologram(H hologram) {
        hologram.setDeleted();
        holograms.remove(hologram);
    }

    public List<H> getHolograms() {
        return unmodifiableHologramsView;
    }

    public void clearAll() {
        Iterator<H> iterator = holograms.iterator();
        while (iterator.hasNext()) {
            H hologram = iterator.next();
            iterator.remove();
            hologram.setDeleted();
        }
    }

    public void onChunkLoad(Chunk chunk) {
        for (H hologram : holograms) {
            hologram.getHologramLocation().onChunkLoad(chunk);
        }
    }

    public void onChunkUnload(Chunk chunk) {
        for (H hologram : holograms) {
            hologram.getHologramLocation().onChunkUnload(chunk);
        }
    }

}
