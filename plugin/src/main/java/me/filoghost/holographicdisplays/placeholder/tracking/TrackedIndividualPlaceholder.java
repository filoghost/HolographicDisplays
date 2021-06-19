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

import java.util.WeakHashMap;

class TrackedIndividualPlaceholder extends TrackedPlaceholder {

    private final @NotNull StandardPlaceholder placeholder;
    private final @NotNull PlaceholderOccurrence placeholderOccurrence;
    private final WeakHashMap<Player, ReplacementHolder> replacementHolderByPlayer;

    TrackedIndividualPlaceholder(@NotNull StandardPlaceholder placeholder, @NotNull PlaceholderOccurrence placeholderOccurrence) {
        super(placeholder.getSource());
        this.placeholder = placeholder;
        this.placeholderOccurrence = placeholderOccurrence;
        this.replacementHolderByPlayer = new WeakHashMap<>();
    }

    @Override
    boolean isIndividual() {
        return true;
    }

    @Override
    @Nullable String updateAndGetReplacement(Player player, long currentTick) throws PlaceholderException {
        return replacementHolderByPlayer
                .computeIfAbsent(player, key -> new ReplacementHolder(placeholder, placeholderOccurrence))
                .updateAndGet(player, currentTick);
    }

}
