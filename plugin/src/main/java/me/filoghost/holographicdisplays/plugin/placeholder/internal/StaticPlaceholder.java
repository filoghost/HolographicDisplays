/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.internal;

import me.filoghost.holographicdisplays.api.placeholder.Placeholder;

public class StaticPlaceholder implements Placeholder {

    private final String text;

    public StaticPlaceholder(String text) {
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
