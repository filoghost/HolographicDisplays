/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import me.filoghost.holographicdisplays.api.placeholder.RegisteredPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PluginName;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class PlaceholderExpansion {

    private final PluginName pluginName;
    private final PlaceholderIdentifier identifier;
    private final RegisteredPlaceholder registeredPlaceholder;

    public PlaceholderExpansion(Plugin plugin, String identifier) {
        this.pluginName = new PluginName(plugin);
        this.identifier = new PlaceholderIdentifier(identifier);
        this.registeredPlaceholder = identifier::toString;
    }

    public PluginName getPluginName() {
        return pluginName;
    }

    public PlaceholderIdentifier getIdentifier() {
        return identifier;
    }

    public RegisteredPlaceholder asRegisteredPlaceholder() {
        return registeredPlaceholder;
    }

    public abstract boolean isIndividual();

    public abstract @Nullable StandardPlaceholder createPlaceholder(String argument) throws PlaceholderException;

}
