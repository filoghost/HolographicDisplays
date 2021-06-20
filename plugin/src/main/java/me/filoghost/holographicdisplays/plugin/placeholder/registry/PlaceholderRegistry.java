/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderReplacer;
import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PluginName;
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

    public void registerIndividualPlaceholderReplacer(Plugin plugin, String identifier, int refreshIntervalTicks, IndividualPlaceholderReplacer placeholderReplacer) {
        registerIndividualPlaceholder(plugin, identifier, new SimpleIndividualPlaceholder(refreshIntervalTicks, placeholderReplacer));
    }

    public void registerIndividualPlaceholder(Plugin plugin, String identifier, IndividualPlaceholder placeholder) {
        registerIndividualPlaceholderFactory(plugin, identifier, (String argument) -> placeholder);
    }

    public void registerIndividualPlaceholderFactory(Plugin plugin, String identifier, IndividualPlaceholderFactory factory) {
        PlaceholderExpansion expansion = new IndividualPlaceholderExpansion(plugin, identifier, factory);
        registerExpansion(expansion);
    }

    public void registerGlobalPlaceholderReplacer(Plugin plugin, String identifier, int refreshIntervalTicks, PlaceholderReplacer placeholderReplacer) {
        registerGlobalPlaceholder(plugin, identifier, new SimpleGlobalPlaceholder(refreshIntervalTicks, placeholderReplacer));
    }

    public void registerGlobalPlaceholder(Plugin plugin, String identifier, Placeholder placeholder) {
        registerGlobalPlaceholderFactory(plugin, identifier, (String argument) -> placeholder);
    }

    public void registerGlobalPlaceholderFactory(Plugin plugin, String identifier, PlaceholderFactory factory) {
        PlaceholderExpansion expansion = new GlobalPlaceholderExpansion(plugin, identifier, factory);
        registerExpansion(expansion);
    }

    private void registerExpansion(PlaceholderExpansion expansion) {
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

    public @Nullable PlaceholderExpansion find(PlaceholderOccurrence textOccurrence) {
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

    public boolean isRegisteredIdentifier(Plugin plugin, String identifier) {
        return placeholderExpansions.contains(new PlaceholderIdentifier(identifier), new PluginName(plugin));
    }

}
