/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.hologram;

import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class InternalHologramManager {

    private final Function<ImmutablePosition, Hologram> hologramFactory;
    private final List<InternalHologram> holograms;

    public InternalHologramManager(Function<ImmutablePosition, Hologram> hologramFactory) {
        this.hologramFactory = hologramFactory;
        this.holograms = new ArrayList<>();
    }

    public @Nullable InternalHologram getHologramByName(String name) {
        for (InternalHologram hologram : holograms) {
            if (hologram.getName().equalsIgnoreCase(name)) {
                return hologram;
            }
        }
        return null;
    }

    public InternalHologram createHologram(String name, ImmutablePosition position) {
        if (getHologramByName(name) != null) {
            throw new IllegalStateException("hologram named \"" + name + "\" already exists");
        }
        InternalHologram hologram = new InternalHologram(hologramFactory, name, position);
        holograms.add(hologram);
        return hologram;
    }

    public List<InternalHologram> getHolograms() {
        return Collections.unmodifiableList(holograms);
    }

    public void deleteHologram(InternalHologram hologram) {
        holograms.remove(hologram);
        hologram.delete();
    }

    public void deleteHolograms() {
        for (InternalHologram hologram : holograms) {
            hologram.delete();
        }
        holograms.clear();
    }

}
