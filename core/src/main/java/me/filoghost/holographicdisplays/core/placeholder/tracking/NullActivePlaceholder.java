/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder.tracking;

import me.filoghost.holographicdisplays.core.placeholder.registry.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

class NullActivePlaceholder extends ActivePlaceholder {

    NullActivePlaceholder(@Nullable PlaceholderExpansion placeholderExpansion) {
        super(placeholderExpansion);
    }

    @Override
    @Nullable String doComputeReplacement(Player player, long currentTick) {
        return null;
    }

    @Override
    boolean isIndividual() {
        return false;
    }

}
