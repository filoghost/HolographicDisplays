/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public class DefaultHolographicDisplaysAPIProvider extends HolographicDisplaysAPIProvider {

    private final Map<Plugin, HolographicDisplaysAPI> apiCache;
    private final Function<Plugin, HolographicDisplaysAPI> apiFactory;

    public DefaultHolographicDisplaysAPIProvider(APIHologramManager apiHologramManager, PlaceholderRegistry placeholderRegistry) {
        this.apiCache = new WeakHashMap<>();
        this.apiFactory = plugin -> new DefaultHolographicDisplaysAPI(plugin, apiHologramManager, placeholderRegistry);
    }

    @Override
    public HolographicDisplaysAPI getHolographicDisplaysAPI(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");

        // Optimization: avoid creating a new instance every time a plugin requests it, in case it never stores a reference
        return apiCache.computeIfAbsent(plugin, apiFactory);
    }

    @Override
    public Position createPosition(World world, double x, double y, double z) {
        Preconditions.notNull(world, "world");
        return createPosition(world.getName(), x, y, z);
    }

    @Override
    public Position createPosition(String worldName, double x, double y, double z) {
        return new ImmutablePosition(worldName, x, y, z);
    }

    @Override
    public Position getPosition(Location location) {
        return ImmutablePosition.of(location);
    }

    @Override
    public Position getPosition(Entity entity) {
        Preconditions.notNull(entity, "entity");
        return getPosition(entity.getLocation());
    }

    @Override
    public Position getPosition(Block block) {
        return createPosition(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

}
