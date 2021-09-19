/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderReplacementSupplier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SimpleIndividualPlaceholder implements IndividualPlaceholder {

    private final int refreshIntervalTicks;
    private final IndividualPlaceholderReplacementSupplier replacementSupplier;

    SimpleIndividualPlaceholder(int refreshIntervalTicks, IndividualPlaceholderReplacementSupplier replacementSupplier) {
        this.refreshIntervalTicks = refreshIntervalTicks;
        this.replacementSupplier = replacementSupplier;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return refreshIntervalTicks;
    }

    @Override
    public String getReplacement(@NotNull Player player, @Nullable String argument) {
        return replacementSupplier.getReplacement(player, argument);
    }

}
