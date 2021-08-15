/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.exception.ConfigValueException;

import java.util.List;

public class AnimationConfig {

    private final double intervalSeconds;
    private final List<String> frames;

    public AnimationConfig(ConfigSection configSection) throws ConfigValueException {
        this.intervalSeconds = configSection.getRequiredDouble("interval-seconds");
        this.frames = configSection.getRequiredStringList("animation-frames");
    }

    public int getIntervalTicks() {
        return Math.max((int) (intervalSeconds * 20.0), 1);
    }

    public List<String> getFrames() {
        return frames;
    }

}
