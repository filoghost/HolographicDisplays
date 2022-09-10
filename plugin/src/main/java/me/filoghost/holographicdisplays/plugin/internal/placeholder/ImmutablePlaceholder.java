/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImmutablePlaceholder implements GlobalPlaceholder, IndividualPlaceholder {

    private final String text;

    public ImmutablePlaceholder(String text) {
        this.text = text;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getReplacement(@Nullable String argument) {
        return text;
    }

    @Override
    public String getReplacement(@NotNull Player player, @Nullable String argument) {
        return text;
    }

}
