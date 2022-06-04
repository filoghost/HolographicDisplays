/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.internal.placeholder;

import com.google.common.collect.ImmutableList;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;

import java.util.List;

public class AnimationPlaceholder implements GlobalPlaceholder {

    private final int refreshIntervalTicks;
    private final ImmutableList<String> frames;
    private int currentIndex;

    public AnimationPlaceholder(int refreshIntervalTicks, List<String> frames) {
        Preconditions.notEmpty(frames, "frames");
        this.refreshIntervalTicks = refreshIntervalTicks;
        this.frames = ImmutableList.copyOf(frames);
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
