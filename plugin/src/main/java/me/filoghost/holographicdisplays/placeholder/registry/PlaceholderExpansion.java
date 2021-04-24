/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.registry;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;
import me.filoghost.holographicdisplays.placeholder.parsing.PlaceholderIdentifier;
import me.filoghost.holographicdisplays.placeholder.parsing.PluginName;
import org.bukkit.plugin.Plugin;

public class PlaceholderExpansion {
    
    private final PluginName pluginName;
    private final PlaceholderIdentifier identifier;
    private final PlaceholderFactory placeholderFactory;
    
    public PlaceholderExpansion(Plugin plugin, String identifier, PlaceholderFactory placeholderFactory) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notEmpty(identifier, "identifier");
        for (char c : identifier.toCharArray()) {
            Preconditions.checkArgument(isValidIdentifierCharacter(c), "identifier contains invalid character '" + c + "'");
        }
        Preconditions.notNull(placeholderFactory, "placeholderFactory");
        
        this.pluginName = new PluginName(plugin);
        this.identifier = new PlaceholderIdentifier(identifier);
        this.placeholderFactory = placeholderFactory;
    }

    private boolean isValidIdentifierCharacter(char c) {
        return ('a' <= c && c <= 'z')
                || ('A' <= c && c <= 'Z')
                || ('0' <= c && c <= '9')
                || c == '-'
                || c == '_';
    }
    
    public PluginName getPluginName() {
        return pluginName;
    }

    public PlaceholderIdentifier getIdentifier() {
        return identifier;
    }

    public PlaceholderFactory getPlaceholderFactory() {
        return placeholderFactory;
    }

}
