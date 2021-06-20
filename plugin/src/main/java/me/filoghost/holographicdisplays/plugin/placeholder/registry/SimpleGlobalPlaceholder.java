/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

class SimpleGlobalPlaceholder implements Placeholder {

    private final int refreshIntervalTicks;
    private final PlaceholderReplacer placeholderReplacer;

    SimpleGlobalPlaceholder(int refreshIntervalTicks, PlaceholderReplacer placeholderReplacer) {
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
