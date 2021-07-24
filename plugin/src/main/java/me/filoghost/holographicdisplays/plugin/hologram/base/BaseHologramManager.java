/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.holographicdisplays.common.hologram.StandardHologram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseHologramManager<H extends StandardHologram> {

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

}
