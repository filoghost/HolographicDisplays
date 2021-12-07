/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder;

import me.filoghost.fcommons.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PlaceholderOccurrence {

    private final PluginName pluginName;
    private final PlaceholderIdentifier identifier;
    private final String argument;

    private final int hashCode; // Cached for performance reasons

    private PlaceholderOccurrence(PluginName pluginName, PlaceholderIdentifier identifier, String argument) {
        this.pluginName = pluginName;
        this.identifier = identifier;
        this.argument = argument;
        this.hashCode = Objects.hash(pluginName, identifier, argument);
    }

    public @Nullable PluginName getPluginName() {
        return pluginName;
    }

    public @NotNull PlaceholderIdentifier getIdentifier() {
        return identifier;
    }

    public @Nullable String getArgument() {
        return argument;
    }

    /*
     * Valid placeholder formats:
     * {identifier}
     * {identifier: argument}
     * {pluginName/identifier}
     * {pluginName/identifier: argument}
     *
     * identifier is required, pluginName and argument are optional
     */
    public static PlaceholderOccurrence parse(String placeholderContent) {
        PluginName pluginName = null;
        String argument = null;
        String identifierString;

        if (placeholderContent.contains(":")) {
            String[] parts = Strings.splitAndTrim(placeholderContent, ":", 2);
            identifierString = parts[0];
            argument = parts[1];
        } else {
            identifierString = placeholderContent;
        }

        if (identifierString.contains("/")) {
            String[] parts = Strings.splitAndTrim(identifierString, "/", 2);
            pluginName = new PluginName(parts[0]);
            identifierString = parts[1];
        }

        PlaceholderIdentifier identifier = new PlaceholderIdentifier(identifierString);
        return new PlaceholderOccurrence(pluginName, identifier, argument);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PlaceholderOccurrence)) {
            return false;
        }

        PlaceholderOccurrence other = (PlaceholderOccurrence) obj;
        return this.hashCode == other.hashCode
                && Objects.equals(this.pluginName, other.pluginName)
                && Objects.equals(this.identifier, other.identifier)
                && Objects.equals(this.argument, other.argument);
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

}
