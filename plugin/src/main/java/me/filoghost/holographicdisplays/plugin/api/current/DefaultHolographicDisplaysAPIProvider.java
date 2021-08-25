/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.HologramPosition;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.WeakHashMap;

public class DefaultHolographicDisplaysAPIProvider extends HolographicDisplaysAPIProvider {

    private final APIHologramManager apiHologramManager;
    private final PlaceholderRegistry placeholderRegistry;

    // Optimization: avoid creating a new instance every time a plugin requires it, in case it never stores a reference
    private final Map<Plugin, HolographicDisplaysAPI> apiInstanceCache;

    public DefaultHolographicDisplaysAPIProvider(APIHologramManager apiHologramManager, PlaceholderRegistry placeholderRegistry) {
        this.apiHologramManager = apiHologramManager;
        this.placeholderRegistry = placeholderRegistry;
        this.apiInstanceCache = new WeakHashMap<>();
    }

    @Override
    public HolographicDisplaysAPI getHolographicDisplaysAPI(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");

        return apiInstanceCache.computeIfAbsent(plugin, pluginKey ->
                new DefaultHolographicDisplaysAPI(pluginKey, apiHologramManager, placeholderRegistry));
    }

    @Override
    public HologramPosition createHologramPosition(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        return new BaseHologramPosition(world.getName(), x, y, z);
    }

    @Override
    public HologramPosition createHologramPosition(String worldName, double x, double y, double z) {
        return new BaseHologramPosition(worldName, x, y, z);
    }

    @Override
    public HologramPosition createHologramPosition(Location location) {
        return new BaseHologramPosition(location);
    }

}
