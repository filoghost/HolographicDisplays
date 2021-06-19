/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.tracking;

import me.filoghost.holographicdisplays.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.placeholder.parsing.PlaceholderOccurrence;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ReplacementHolder {

    private final @NotNull StandardPlaceholder placeholder;
    private final @NotNull PlaceholderOccurrence placeholderOccurrence;

    private @Nullable String currentReplacement;
    private long lastUpdateTick = -1;

    ReplacementHolder(@NotNull StandardPlaceholder placeholder, @NotNull PlaceholderOccurrence placeholderOccurrence) {
        this.placeholder = placeholder;
        this.placeholderOccurrence = placeholderOccurrence;
    }

    @Nullable String updateAndGet(Player player, long currentTick) throws PlaceholderException {
        if (needsRefresh(currentTick)) {
            currentReplacement = placeholder.getReplacement(player, placeholderOccurrence.getArgument());
            lastUpdateTick = currentTick;
        }
        return currentReplacement;
    }

    private boolean needsRefresh(long currentTick) throws PlaceholderException {
        if (lastUpdateTick == currentTick) {
            return false; // No need to refresh
        }

        if (lastUpdateTick == -1) {
            return true; // Force at least the initial refresh
        }

        return currentTick - lastUpdateTick >= placeholder.getRefreshIntervalTicks();
    }

}
