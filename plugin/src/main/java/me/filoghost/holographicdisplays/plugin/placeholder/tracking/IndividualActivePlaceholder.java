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

import java.util.WeakHashMap;

class IndividualActivePlaceholder extends ActivePlaceholder {

    private final @NotNull StandardPlaceholder placeholder;
    private final @NotNull PlaceholderOccurrence placeholderOccurrence;
    private final WeakHashMap<Player, ReplacementHolder> replacementHolderByPlayer;

    IndividualActivePlaceholder(@NotNull StandardPlaceholder placeholder, @NotNull PlaceholderOccurrence placeholderOccurrence) {
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
    @Nullable String doUpdateAndGetReplacement(Player player, long currentTick) throws PlaceholderException {
        return replacementHolderByPlayer
                .computeIfAbsent(player, key -> new ReplacementHolder(placeholder, placeholderOccurrence))
                .updateAndGet(player, currentTick);
    }

}
