/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder;

import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class StandardPlaceholder {

    private final @NotNull PlaceholderExpansion source;

    protected StandardPlaceholder(@NotNull PlaceholderExpansion source) {
        this.source = source;
    }

    public final @NotNull PlaceholderExpansion getSource() {
        return source;
    }

    public boolean isIndividual() {
        return source.isIndividual();
    }

    public final int getRefreshIntervalTicks() throws PlaceholderException {
        try {
            return doGetRefreshIntervalTicks();
        } catch (Throwable t) {
            throw new PlaceholderException(t, getSource());
        }
    }

    public final @Nullable String getReplacement(Player player, @Nullable String argument) throws PlaceholderException {
        try {
            return doGetReplacement(player, argument);
        } catch (Throwable t) {
            throw new PlaceholderException(t, getSource());
        }
    }

    /*
     * Below methods may use externally provided objects which can throw any exception
     */
    
    protected abstract int doGetRefreshIntervalTicks() throws Throwable;

    protected abstract @Nullable String doGetReplacement(Player player, @Nullable String argument) throws Throwable;

}
