/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;

public class ImmutablePlaceholder implements GlobalPlaceholder {

    private final String text;

    public ImmutablePlaceholder(String text) {
        this.text = text;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getReplacement(String argument) {
        return text;
    }

}
