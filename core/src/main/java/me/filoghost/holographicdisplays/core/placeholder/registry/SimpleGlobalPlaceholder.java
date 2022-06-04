/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder.registry;

import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderReplaceFunction;

class SimpleGlobalPlaceholder implements GlobalPlaceholder {

    private final int refreshIntervalTicks;
    private final GlobalPlaceholderReplaceFunction replaceFunction;

    SimpleGlobalPlaceholder(int refreshIntervalTicks, GlobalPlaceholderReplaceFunction replaceFunction) {
        this.refreshIntervalTicks = refreshIntervalTicks;
        this.replaceFunction = replaceFunction;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return refreshIntervalTicks;
    }

    @Override
    public String getReplacement(String argument) {
        return replaceFunction.getReplacement(argument);
    }

}
