/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.internal.BackendAPI;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.object.api.APIHologramManager;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class DefaultBackendAPI extends BackendAPI {

    private final APIHologramManager apiHologramManager;
    private final NMSManager nmsManager;
    private final PlaceholderRegistry placeholderRegistry;

    public DefaultBackendAPI(APIHologramManager apiHologramManager, NMSManager nmsManager, PlaceholderRegistry placeholderRegistry) {
        this.apiHologramManager = apiHologramManager;
        this.nmsManager = nmsManager;
        this.placeholderRegistry = placeholderRegistry;
    }

    @Override
    public Hologram createHologram(Plugin plugin, Location source) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(source, "source");
        Preconditions.notNull(source.getWorld(), "source's world");
        Preconditions.checkState(Bukkit.isPrimaryThread(), "Async hologram creation");
        
        return apiHologramManager.createHologram(source, plugin);
    }
    
    @Override
    public void registerPlaceholder(Plugin plugin, String identifier, int refreshIntervalTicks, PlaceholderReplacer replacer) {
        Preconditions.notNull(identifier, "identifier");
        Preconditions.checkArgument(refreshIntervalTicks >= 0, "refreshIntervalTicks should be positive");
        Preconditions.notNull(replacer, "replacer");
        
        placeholderRegistry.registerReplacer(plugin, identifier, refreshIntervalTicks, replacer);
    }

    @Override
    public boolean isHologramEntity(Entity bukkitEntity) {
        Preconditions.notNull(bukkitEntity, "bukkitEntity");
        return nmsManager.isNMSEntityBase(bukkitEntity);
    }

    @Override
    public Collection<Hologram> getHolograms(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        return apiHologramManager.getHologramsByPlugin(plugin);
    }

    @Override
    public Collection<String> getRegisteredPlaceholders(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        return placeholderRegistry.getRegisteredIdentifiers(plugin);
    }

    @Override
    public void unregisterPlaceholder(Plugin plugin, String identifier) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(identifier, "identifier");
        placeholderRegistry.unregister(plugin, identifier);
    }

    @Override
    public void unregisterPlaceholders(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        placeholderRegistry.unregisterAll(plugin);
    }

}
