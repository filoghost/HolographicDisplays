/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import me.filoghost.fcommons.collection.CaseInsensitiveString;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholderFactory;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholderReplacementSupplier;
import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholderReplacementSupplier;
import me.filoghost.holographicdisplays.api.beta.placeholder.RegisteredPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.plugin.placeholder.PluginName;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class PlaceholderRegistry {

    private final Table<PlaceholderIdentifier, PluginName, PlaceholderExpansion> placeholderExpansions;
    private final Table<CaseInsensitiveString, PluginName, LegacyGlobalPlaceholderExpansion> legacyPlaceholderExpansions;
    private final AtomicLong version;

    public PlaceholderRegistry() {
        this.placeholderExpansions = HashBasedTable.create();
        this.legacyPlaceholderExpansions = HashBasedTable.create();
        this.version = new AtomicLong();
    }

    public long getVersion() {
        return version.get();
    }

    public void registerIndividualPlaceholder(
            Plugin plugin, String identifier, int refreshIntervalTicks, IndividualPlaceholderReplacementSupplier replacementSupplier) {
        registerIndividualPlaceholder(plugin, identifier, new SimpleIndividualPlaceholder(refreshIntervalTicks, replacementSupplier));
    }

    public void registerIndividualPlaceholder(Plugin plugin, String identifier, IndividualPlaceholder placeholder) {
        registerIndividualPlaceholderFactory(plugin, identifier, (String argument) -> placeholder);
    }

    public void registerIndividualPlaceholderFactory(Plugin plugin, String identifier, IndividualPlaceholderFactory factory) {
        PlaceholderExpansion expansion = new IndividualPlaceholderExpansion(plugin, identifier, factory);
        registerExpansion(expansion);
    }

    public void registerGlobalPlaceholder(
            Plugin plugin, String identifier, int refreshIntervalTicks, GlobalPlaceholderReplacementSupplier replacementSupplier) {
        registerGlobalPlaceholder(plugin, identifier, new SimpleGlobalPlaceholder(refreshIntervalTicks, replacementSupplier));
    }

    public void registerGlobalPlaceholder(Plugin plugin, String identifier, GlobalPlaceholder placeholder) {
        registerGlobalPlaceholderFactory(plugin, identifier, (String argument) -> placeholder);
    }

    public void registerGlobalPlaceholderFactory(Plugin plugin, String identifier, GlobalPlaceholderFactory factory) {
        PlaceholderExpansion expansion = new GlobalPlaceholderExpansion(plugin, identifier, factory);
        registerExpansion(expansion);
    }

    private void registerExpansion(PlaceholderExpansion expansion) {
        placeholderExpansions.put(expansion.getCaseInsensitiveIdentifier(), expansion.getPluginName(), expansion);

        version.incrementAndGet();
    }

    public void unregisterAll(Plugin plugin) {
        placeholderExpansions.column(new PluginName(plugin)).clear();

        version.incrementAndGet();
    }

    public void unregister(Plugin plugin, String identifier) {
        placeholderExpansions.remove(new PlaceholderIdentifier(identifier), new PluginName(plugin));

        version.incrementAndGet();
    }

    public @Nullable PlaceholderExpansion find(PlaceholderOccurrence textOccurrence) {
        PluginName pluginName = textOccurrence.getPluginName();
        PlaceholderIdentifier identifier = textOccurrence.getIdentifier();
        PlaceholderExpansion result;

        if (pluginName != null) {
            // Find exact entry if plugin name is specified
            result = placeholderExpansions.get(identifier, pluginName);
        } else {
            // Otherwise find any match with the given identifier
            result = Iterables.getFirst(placeholderExpansions.row(identifier).values(), null);
        }

        if (result == null && !legacyPlaceholderExpansions.isEmpty()) {
            result = Iterables.getFirst(legacyPlaceholderExpansions.row(textOccurrence.getUnparsedContent()).values(), null);
        }
        return result;
    }

    public List<RegisteredPlaceholder> getRegisteredPlaceholders(Plugin plugin) {
        PluginName pluginName = new PluginName(plugin);

        return new ArrayList<>(placeholderExpansions.column(pluginName).values());
    }

    public boolean isRegisteredIdentifier(Plugin plugin, String identifier) {
        return placeholderExpansions.contains(new PlaceholderIdentifier(identifier), new PluginName(plugin));
    }

    public void registerLegacyPlaceholder(
            Plugin plugin,
            String textPlaceholder,
            int refreshIntervalTicks,
            GlobalPlaceholderReplacementSupplier replacementSupplier) {
        String identifier = convertToNewFormat(textPlaceholder);
        if (!identifier.equals(textPlaceholder)) {
            Log.warning("The plugin " + plugin.getName() + " registered the placeholder " + textPlaceholder
                    + " with the old v2 API, but it doesn't comply with the new format. In order to display it,"
                    + " you must use {" + textPlaceholder + "} instead.");
        }
        GlobalPlaceholder placeholder = new SimpleGlobalPlaceholder(refreshIntervalTicks, replacementSupplier);
        GlobalPlaceholderFactory placeholderFactory = (String argument) -> placeholder;
        LegacyGlobalPlaceholderExpansion expansion = new LegacyGlobalPlaceholderExpansion(
                plugin,
                identifier,
                placeholderFactory,
                textPlaceholder);

        legacyPlaceholderExpansions.put(new CaseInsensitiveString(identifier), new PluginName(plugin), expansion);

        version.incrementAndGet();
    }

    public void unregisterLegacyPlaceholder(Plugin plugin, String textPlaceholder) {
        String identifier = convertToNewFormat(textPlaceholder);
        legacyPlaceholderExpansions.remove(new CaseInsensitiveString(identifier), new PluginName(plugin));

        version.incrementAndGet();
    }

    public void unregisterAllLegacyPlaceholders(Plugin plugin) {
        legacyPlaceholderExpansions.column(new PluginName(plugin)).clear();

        version.incrementAndGet();
    }

    public boolean isRegisteredLegacyPlaceholder(Plugin plugin, String textPlaceholder) {
        String identifier = convertToNewFormat(textPlaceholder);
        return legacyPlaceholderExpansions.contains(new CaseInsensitiveString(identifier), new PluginName(plugin));
    }

    public Collection<LegacyGlobalPlaceholderExpansion> getRegisteredLegacyPlaceholders(Plugin plugin) {
        return legacyPlaceholderExpansions.column(new PluginName(plugin)).values();
    }

    private String convertToNewFormat(String textPlaceholder) {
        String identifier;
        if (textPlaceholder.startsWith("{") && textPlaceholder.endsWith("}")) {
            // The placeholder already had the correct format, remove the curly braces
            identifier = textPlaceholder.substring(1, textPlaceholder.length() - 1);
        } else {
            // The placeholder will be wrapped with curly braces to partially maintain compatibility
            identifier = textPlaceholder;
        }
        return identifier;
    }

}
