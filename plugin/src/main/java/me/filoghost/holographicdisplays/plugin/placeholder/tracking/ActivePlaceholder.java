/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderExpansion;
import me.filoghost.holographicdisplays.plugin.tick.TickExpiringValue;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

abstract class ActivePlaceholder implements TickExpiringValue {

    private final @Nullable PlaceholderExpansion source;
    private long lastRequestTick;

    ActivePlaceholder(@Nullable PlaceholderExpansion source) {
        this.source = source;
        this.lastRequestTick = -1;
    }

    final @Nullable PlaceholderExpansion getSource() {
        return source;
    }

    @Override
    public final long getLastUseTick() {
        return lastRequestTick;
    }

    final @Nullable String computeReplacement(@Nullable Player player, long currentTick) throws PlaceholderException {
        this.lastRequestTick = currentTick;
        return doComputeReplacement(player, currentTick);
    }

    abstract boolean isIndividual();

    abstract @Nullable String doComputeReplacement(Player player, long currentTick) throws PlaceholderException;

}
