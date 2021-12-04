/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.registry;

import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholderReplacementSupplier;

class SimpleGlobalPlaceholder implements GlobalPlaceholder {

    private final int refreshIntervalTicks;
    private final GlobalPlaceholderReplacementSupplier replacementSupplier;

    SimpleGlobalPlaceholder(int refreshIntervalTicks, GlobalPlaceholderReplacementSupplier replacementSupplier) {
        this.refreshIntervalTicks = refreshIntervalTicks;
        this.replacementSupplier = replacementSupplier;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return refreshIntervalTicks;
    }

    @Override
    public String getReplacement(String argument) {
        return replacementSupplier.getReplacement(argument);
    }

}
