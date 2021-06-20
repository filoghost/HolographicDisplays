/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.WeakHashMap;

public class DefaultHolographicDisplaysAPIProvider extends HolographicDisplaysAPIProvider {

    private final APIHologramManager apiHologramManager;
    private final NMSManager nmsManager;
    private final PlaceholderRegistry placeholderRegistry;
    
    // Optimization: avoid creating a new instance every time a plugin requires it, in case it never stores a reference
    private final Map<Plugin, HolographicDisplaysAPI> apiInstanceCache;

    public DefaultHolographicDisplaysAPIProvider(
            APIHologramManager apiHologramManager,
            NMSManager nmsManager,
            PlaceholderRegistry placeholderRegistry) {
        this.apiHologramManager = apiHologramManager;
        this.nmsManager = nmsManager;
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
    public boolean isHologramEntity(Entity entity) {
        Preconditions.notNull(entity, "entity");
        
        return nmsManager.isNMSEntityBase(entity);
    }

}
