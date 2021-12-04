/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api.internal;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.api.beta.internal.HolographicDisplaysAPIProvider;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Collection;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
@Internal
public abstract class HologramsAPIProvider {

    private static HologramsAPIProvider implementation;

    @Deprecated
    public static void setImplementation(HologramsAPIProvider implementation) {
        HologramsAPIProvider.implementation = implementation;
    }

    @Deprecated
    public static HologramsAPIProvider getImplementation() {
        if (implementation == null) {
            throw new IllegalStateException(HolographicDisplaysAPIProvider.ERROR_IMPLEMENTATION_NOT_SET);
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
