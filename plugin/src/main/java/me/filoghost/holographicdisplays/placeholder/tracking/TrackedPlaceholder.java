/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.tracking;

import me.filoghost.holographicdisplays.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

abstract class TrackedPlaceholder {
    
    private final @Nullable PlaceholderExpansion source;

    TrackedPlaceholder(@Nullable PlaceholderExpansion source) {
        this.source = source;
    }

    final @Nullable PlaceholderExpansion getSource() {
        return source;
    }

    abstract boolean isIndividual();

    abstract @Nullable String updateAndGetReplacement(Player player, long currentTick) throws PlaceholderException;

}
