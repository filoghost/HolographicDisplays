/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api;

import me.filoghost.holographicdisplays.api.internal.BackendAPI;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * This the main class of the <b>Holographic Displays API</b>.
 * It provides methods to create holograms and to register custom placeholders.
 */
public class HologramsAPI {
    
    
    private HologramsAPI() {
        // No constructor needed.
    }
    
    
    /**
     * Creates a hologram at given location.
     * 
     * @param plugin the plugin that creates it
     * @param source the location where it will appear
     * @return the new hologram created
     */
    public static Hologram createHologram(Plugin plugin, Location source) {
        return BackendAPI.getImplementation().createHologram(plugin, source);
    }
    
    
    /**
     * Finds all the holograms created by a given plugin.
     * 
     * @param plugin the plugin to search for in holograms
     * @return the holograms created by a plugin. the Collection is a copy
     * and modifying it has no effect on the holograms.
     */
    public static Collection<Hologram> getHolograms(Plugin plugin) {
        return BackendAPI.getImplementation().getHolograms(plugin);
    }
    
    public static void registerPlaceholder(Plugin plugin, String identifier, int refreshIntervalTicks, PlaceholderReplacer replacer) {
        BackendAPI.getImplementation().registerPlaceholder(plugin, identifier, refreshIntervalTicks, replacer);
    }
    
    
    /**
     * Returns all the placeholder identifiers registered by a given plugin.
     * 
     * @param plugin the plugin to search for
     * @return a collection of placeholder identifiers registered by the plugin
     */
    public static Collection<String> getRegisteredPlaceholders(Plugin plugin) {
        return BackendAPI.getImplementation().getRegisteredPlaceholders(plugin);
    }
    
    
    /**
     * Unregister a placeholder created by a plugin.
     * 
     * @param plugin the plugin that owns the placeholder
     * @param identifier the identifier of the placeholder to remove
     */
    public static void unregisterPlaceholder(Plugin plugin, String identifier) {
        BackendAPI.getImplementation().unregisterPlaceholder(plugin, identifier);
    }
    
    
    /**
     * Resets and removes all the placeholders registered by a plugin. This is useful
     * when you have configurable placeholders and you want to remove all of them.
     * 
     * @param plugin the plugin that owns the placeholders
     */
    public static void unregisterPlaceholders(Plugin plugin) {
        BackendAPI.getImplementation().unregisterPlaceholders(plugin);
    }
    
    
    /**
     * Checks if an entity is part of a hologram.
     * 
     * @param bukkitEntity the entity to check
     * @return true if the entity is a part of a hologram
     */
    public static boolean isHologramEntity(Entity bukkitEntity) {
        return BackendAPI.getImplementation().isHologramEntity(bukkitEntity);
    }

}
