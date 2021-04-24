/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.registry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.placeholder.parsing.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.placeholder.parsing.PluginName;
import me.filoghost.holographicdisplays.placeholder.util.SimplePlaceholder;
import me.filoghost.holographicdisplays.placeholder.util.SingletonPlaceholderFactory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderRegistry {
    
    private final Table<PlaceholderIdentifier, PluginName, PlaceholderExpansion> placeholderExpansions;
    private Runnable changeListener;
    
    public PlaceholderRegistry() {
        this.placeholderExpansions = HashBasedTable.create();
    }

    public void setChangeListener(Runnable changeListener) {
        this.changeListener = changeListener;
    }

    public void registerReplacer(Plugin plugin, String identifier, int refreshIntervalTicks, PlaceholderReplacer placeholderReplacer) {
        register(plugin, identifier, new SimplePlaceholder(refreshIntervalTicks, placeholderReplacer));
    }
    
    public void register(Plugin plugin, String identifier, Placeholder placeholder) {
        registerFactory(plugin, identifier, new SingletonPlaceholderFactory(placeholder));
    }
    
    public void registerFactory(Plugin plugin, String identifier, PlaceholderFactory placeholderFactory) {
        PlaceholderExpansion expansion = new PlaceholderExpansion(plugin, identifier, placeholderFactory);
        placeholderExpansions.put(expansion.getIdentifier(), expansion.getPluginName(), expansion);
        
        changeListener.run();
    }
    
    public void unregisterAll(Plugin plugin) {
        placeholderExpansions.column(new PluginName(plugin)).clear();
        
        changeListener.run();
    }

    public void unregister(Plugin plugin, String identifier) {
        placeholderExpansions.remove(new PlaceholderIdentifier(identifier), new PluginName(plugin));
        
        changeListener.run();
    }

    @Nullable
    public PlaceholderExpansion findBestMatch(PlaceholderOccurrence textOccurrence) {
        PluginName pluginName = textOccurrence.getPluginName();
        PlaceholderIdentifier identifier = textOccurrence.getIdentifier();
        
        if (pluginName != null) {
            // Find exact entry if plugin name is specified
            return placeholderExpansions.get(identifier, pluginName);

        } else {
            // Otherwise find any match with the given identifier
            return Iterables.getFirst(placeholderExpansions.row(identifier).values(), null);
        }
    }

    public List<String> getRegisteredIdentifiers(Plugin plugin) {
        PluginName pluginName = new PluginName(plugin);
        List<String> identifiers = new ArrayList<>();

        for (PlaceholderExpansion expansion : placeholderExpansions.column(pluginName).values()) {
            identifiers.add(expansion.getIdentifier().toString());
        }

        return identifiers;
    }

}
