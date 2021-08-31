/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderReplacer;

class SimpleGlobalPlaceholder implements GlobalPlaceholder {

    private final int refreshIntervalTicks;
    private final GlobalPlaceholderReplacer placeholderReplacer;

    SimpleGlobalPlaceholder(int refreshIntervalTicks, GlobalPlaceholderReplacer placeholderReplacer) {
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
