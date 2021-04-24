/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.util;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

public class SimplePlaceholder implements Placeholder {

    private final int refreshIntervalTicks;
    private final PlaceholderReplacer placeholderReplacer;

    public SimplePlaceholder(int refreshIntervalTicks, PlaceholderReplacer placeholderReplacer) {
        Preconditions.checkArgument(refreshIntervalTicks >= 0, "refreshIntervalTicks cannot be negative");
        Preconditions.notNull(placeholderReplacer, "placeholderReplacer");
        this.refreshIntervalTicks = refreshIntervalTicks;
        this.placeholderReplacer = placeholderReplacer;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return refreshIntervalTicks;
    }

    @Override
    public String getReplacement(String argument) {
        return placeholderReplacer.getReplacement(argument);
    }

}
