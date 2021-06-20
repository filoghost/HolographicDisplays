/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.internal;

import me.filoghost.holographicdisplays.api.placeholder.Placeholder;

import java.util.List;

public class AnimationPlaceholder implements Placeholder {

    private final int refreshIntervalTicks;
    private final List<String> frames;
    private int currentIndex;
    
    public AnimationPlaceholder(int refreshIntervalTicks, List<String> frames) {
        this.frames = frames;
        this.refreshIntervalTicks = refreshIntervalTicks;
    }

    @Override
    public int getRefreshIntervalTicks() {
        return refreshIntervalTicks;
    }
    
    @Override
    public String getReplacement(String argument) {
        String result = frames.get(currentIndex);

        currentIndex++;
        if (currentIndex >= frames.size()) {
            currentIndex = 0;
        }

        return result;
    }

}
