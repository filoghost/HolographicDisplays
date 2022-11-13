/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.core.base.ImmutablePosition;
import me.filoghost.holographicdisplays.core.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class DefaultHolographicDisplaysAPIProvider extends HolographicDisplaysAPIProvider {

    private final APIHologramManager apiHologramManager;
    private final PlaceholderRegistry placeholderRegistry;

    public DefaultHolographicDisplaysAPIProvider(APIHologramManager apiHologramManager, PlaceholderRegistry placeholderRegistry) {
        this.apiHologramManager = apiHologramManager;
        this.placeholderRegistry = placeholderRegistry;
    }

    @Override
    public HolographicDisplaysAPI getHolographicDisplaysAPI(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        return new DefaultHolographicDisplaysAPI(plugin, apiHologramManager, placeholderRegistry);
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
