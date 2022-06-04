/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder.registry;

import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderReplaceFunction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SimpleIndividualPlaceholder implements IndividualPlaceholder {

    private final int refreshIntervalTicks;
    private final IndividualPlaceholderReplaceFunction replaceFunction;

    SimpleIndividualPlaceholder(int refreshIntervalTicks, IndividualPlaceholderReplaceFunction replaceFunction) {
        this.refreshIntervalTicks = refreshIntervalTicks;
        this.replaceFunction = replaceFunction;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return refreshIntervalTicks;
    }

    @Override
    public String getReplacement(@NotNull Player player, @Nullable String argument) {
        return replaceFunction.getReplacement(player, argument);
    }

}
