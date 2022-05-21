/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder.registry;

import me.filoghost.holographicdisplays.core.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.core.placeholder.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.core.placeholder.PluginName;
import me.filoghost.holographicdisplays.core.placeholder.StandardPlaceholder;
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
