/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.api.internal.HologramsAPIProvider;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public class HologramsAPI {

    private HologramsAPI() {}

    @Deprecated
    public static Hologram createHologram(Plugin plugin, Location source) {
        return HologramsAPIProvider.getImplementation().createHologram(plugin, source);
    }

    @Deprecated
    public static Collection<Hologram> getHolograms(Plugin plugin) {
        return HologramsAPIProvider.getImplementation().getHolograms(plugin);
    }

    @Deprecated
    public static boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
        return HologramsAPIProvider.getImplementation().registerPlaceholder(plugin, textPlaceholder, refreshRate, replacer);
    }
    
    @Deprecated
    public static Collection<String> getRegisteredPlaceholders(Plugin plugin) {
        return HologramsAPIProvider.getImplementation().getRegisteredPlaceholders(plugin);
    }

    @Deprecated
    public static boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
        return HologramsAPIProvider.getImplementation().unregisterPlaceholder(plugin, textPlaceholder);
    }
    
    @Deprecated
    public static void unregisterPlaceholders(Plugin plugin) {
        HologramsAPIProvider.getImplementation().unregisterPlaceholders(plugin);
    }

    @Deprecated
    public static boolean isHologramEntity(Entity bukkitEntity) {
        return HologramsAPIProvider.getImplementation().isHologramEntity(bukkitEntity);
    }

}
