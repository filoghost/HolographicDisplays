/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.StandardPlaceholder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GlobalPlaceholderExpansion extends PlaceholderExpansion {

    private final PlaceholderFactory placeholderFactory;

    GlobalPlaceholderExpansion(Plugin plugin, String identifier, PlaceholderFactory placeholderFactory) {
        super(plugin, identifier);
        this.placeholderFactory = placeholderFactory;
    }

    @Override
    public boolean isIndividual() {
        return false;
    }

    @Override
    public @Nullable StandardPlaceholder createPlaceholder(String argument) throws PlaceholderException {
        Placeholder placeholder;
        try {
            placeholder = placeholderFactory.getPlaceholder(argument);
        } catch (Throwable t) {
            throw new PlaceholderException(t, this);
        }

        if (placeholder != null) {
            return new GlobalStandardPlaceholder(placeholder, this);
        } else {
            return null;
        }
    }


    private static class GlobalStandardPlaceholder extends StandardPlaceholder {

        private final @NotNull Placeholder placeholder;

        GlobalStandardPlaceholder(@NotNull Placeholder placeholder, @NotNull GlobalPlaceholderExpansion source) {
            super(source);
            this.placeholder = placeholder;
        }

        @Override
        protected int doGetRefreshIntervalTicks() {
            return placeholder.getRefreshIntervalTicks();
        }

        @Override
        protected @Nullable String doGetReplacement(Player player, @Nullable String argument) {
            return placeholder.getReplacement(argument);
        }

    }

}
