/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderOccurrence;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TrackedGlobalPlaceholder extends TrackedPlaceholder {

    private final ReplacementHolder replacementHolder;

    TrackedGlobalPlaceholder(@NotNull StandardPlaceholder placeholder, @NotNull PlaceholderOccurrence placeholderOccurrence) {
        super(placeholder.getSource());
        this.replacementHolder = new ReplacementHolder(placeholder, placeholderOccurrence);
    }

    @Override
    boolean isIndividual() {
        return false;
    }

    @Override
    @Nullable String updateAndGetReplacement(Player player, long currentTick) throws PlaceholderException {
        return replacementHolder.updateAndGet(player, currentTick);
    }


}
