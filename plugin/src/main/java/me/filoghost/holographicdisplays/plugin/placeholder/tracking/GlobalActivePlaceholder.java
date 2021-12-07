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

class GlobalActivePlaceholder extends ActivePlaceholder {

    private final ReplacementHolder replacementHolder;

    GlobalActivePlaceholder(@NotNull StandardPlaceholder placeholder, @NotNull PlaceholderOccurrence placeholderOccurrence) {
        super(placeholder.getSource());
        this.replacementHolder = new ReplacementHolder(placeholder, placeholderOccurrence);
    }

    @Override
    boolean isIndividual() {
        return false;
    }

    @Override
    @Nullable String doComputeReplacement(Player player, long currentTick) throws PlaceholderException {
        return replacementHolder.computeReplacement(player, currentTick);
    }


}
