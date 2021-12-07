/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderOccurrence;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.WeakHashMap;
import java.util.function.Function;

class IndividualActivePlaceholder extends ActivePlaceholder {

    private final WeakHashMap<Player, ReplacementHolder> replacementHolderByPlayer;
    private final Function<Player, ReplacementHolder> mappingFunction;

    IndividualActivePlaceholder(@NotNull StandardPlaceholder placeholder, @NotNull PlaceholderOccurrence placeholderOccurrence) {
        super(placeholder.getSource());
        this.replacementHolderByPlayer = new WeakHashMap<>();
        this.mappingFunction = key -> new ReplacementHolder(placeholder, placeholderOccurrence);
    }

    @Override
    boolean isIndividual() {
        return true;
    }

    @Override
    @Nullable String doComputeReplacement(Player player, long currentTick) throws PlaceholderException {
        return replacementHolderByPlayer
                .computeIfAbsent(player, mappingFunction)
                .computeReplacement(player, currentTick);
    }

}
