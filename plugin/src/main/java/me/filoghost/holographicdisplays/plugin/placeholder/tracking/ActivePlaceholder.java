/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

abstract class ActivePlaceholder {

    private final @Nullable PlaceholderExpansion source;
    private long lastRequestTick;

    ActivePlaceholder(@Nullable PlaceholderExpansion source) {
        this.source = source;
        this.lastRequestTick = -1;
    }

    final @Nullable PlaceholderExpansion getSource() {
        return source;
    }

    final long getLastRequestTick() {
        return lastRequestTick;
    }

    final @Nullable String updateAndGetReplacement(Player player, long currentTick) throws PlaceholderException {
        this.lastRequestTick = currentTick;
        return doUpdateAndGetReplacement(player, currentTick);
    }

    abstract boolean isIndividual();

    abstract @Nullable String doUpdateAndGetReplacement(Player player, long currentTick) throws PlaceholderException;

}
