/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.config.mapped.MappedConfig;
import me.filoghost.fcommons.config.mapped.Path;

import java.util.List;

public class AnimationConfig implements MappedConfig {

    @Path("interval-seconds")
    private double intervalSeconds;

    @Path("animation-frames")
    private List<String> frames;

    public int getIntervalTicks() {
        return Math.max((int) (intervalSeconds * 20.0), 1);
    }

    public List<String> getFrames() {
        return frames;
    }

}
