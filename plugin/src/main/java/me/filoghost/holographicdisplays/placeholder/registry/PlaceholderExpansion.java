/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.registry;

import me.filoghost.holographicdisplays.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.placeholder.parsing.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.placeholder.parsing.PluginName;
import me.filoghost.holographicdisplays.placeholder.PlaceholderException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class PlaceholderExpansion {
    
    private final PluginName pluginName;
    private final PlaceholderIdentifier identifier;
    
    public PlaceholderExpansion(Plugin plugin, String identifier) {
        this.pluginName = new PluginName(plugin);
        this.identifier = new PlaceholderIdentifier(identifier);
    }
    
    public PluginName getPluginName() {
        return pluginName;
    }

    public PlaceholderIdentifier getIdentifier() {
        return identifier;
    }

    public abstract boolean isIndividual();

    public abstract @Nullable StandardPlaceholder createPlaceholder(String argument) throws PlaceholderException;

}
