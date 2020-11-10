/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.internal;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public abstract class BackendAPI {
    
    private static BackendAPI implementation;

    @Deprecated
    public static void setImplementation(BackendAPI implementation) {
        BackendAPI.implementation = implementation;
    }

    @Deprecated
    public static BackendAPI getImplementation() {
        if (implementation == null) {
            throw new IllegalStateException("No API implementation set. Is Holographic Displays enabled?");
        }
        
        return implementation;
    }

    @Deprecated
    public abstract Hologram createHologram(Plugin plugin, Location source);

    @Deprecated
    public abstract Collection<Hologram> getHolograms(Plugin plugin);

    @Deprecated
    public abstract boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer);

    @Deprecated
    public abstract Collection<String> getRegisteredPlaceholders(Plugin plugin);

    @Deprecated
    public abstract boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder);

    @Deprecated
    public abstract void unregisterPlaceholders(Plugin plugin);

    @Deprecated
    public abstract boolean isHologramEntity(Entity bukkitEntity);

}
