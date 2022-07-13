/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public abstract class BaseHologramManager<H extends BaseHologram> {

    private final List<H> holograms = Collections.synchronizedList(new ArrayList<>());
    private final List<H> unmodifiableHologramsView = Collections.unmodifiableList(holograms);

    protected void addHologram(H hologram) {
        if (hologram == null) {
            throw new IllegalArgumentException("hologram cannot be null!");
        }
        holograms.add(hologram);
    }

    public List<H> getHolograms() {
        return unmodifiableHologramsView;
    }

    public void deleteHologram(H hologram) {
        hologram.setDeleted();
        holograms.remove(hologram);
    }

    public void deleteHologramsIf(Predicate<H> condition) {
        synchronized (holograms) {
            Iterator<H> iterator = holograms.iterator();
            while (iterator.hasNext()) {
                H hologram = iterator.next();
                if (condition.test(hologram)) {
                    iterator.remove();
                    hologram.setDeleted();
                }
            }
        }
    }

    public void deleteHolograms() {
        synchronized (holograms) {
            Iterator<H> iterator = holograms.iterator();
            while (iterator.hasNext()) {
                H hologram = iterator.next();
                iterator.remove();
                hologram.setDeleted();
            }
        }
    }

    public void onWorldLoad(World world) {
        synchronized (holograms) {
            for (H hologram : holograms) {
                hologram.onWorldLoad(world);
            }
        }
    }

    public void onWorldUnload(World world) {
        synchronized (holograms) {
            for (H hologram : holograms) {
                hologram.onWorldUnload(world);
            }
        }
    }

    public void onChunkLoad(Chunk chunk) {
        synchronized (holograms) {
            for (H hologram : holograms) {
                hologram.onChunkLoad(chunk);
            }
        }
    }

    public void onChunkUnload(Chunk chunk) {
        synchronized (holograms) {
            for (H hologram : holograms) {
                hologram.onChunkUnload(chunk);
            }
        }
    }

}
