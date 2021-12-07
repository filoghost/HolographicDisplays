/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import me.filoghost.holographicdisplays.api.beta.placeholder.RegisteredPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.plugin.placeholder.PluginName;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlaceholderExpansion implements RegisteredPlaceholder {

    private final PluginName pluginName;
    private final PlaceholderIdentifier identifier;

    public PlaceholderExpansion(Plugin plugin, String identifier) {
        this.pluginName = new PluginName(plugin);
        this.identifier = new PlaceholderIdentifier(identifier);
    }

    public PluginName getPluginName() {
        return pluginName;
    }

    public PlaceholderIdentifier getCaseInsensitiveIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier.toString();
    }

    public abstract boolean isIndividual();

    public abstract @Nullable StandardPlaceholder createPlaceholder(String argument) throws PlaceholderException;

}
