/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.test;

import me.filoghost.holographicdisplays.plugin.api.v2.V2HologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.listener.LineClickListener;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.plugin.tick.TickClock;

import static org.mockito.Mockito.*;

public class TestV2HologramManager extends V2HologramManager {

    public TestV2HologramManager() {
        super(new LineTrackerManager(
                new TestNMSManager(),
                mock(ActivePlaceholderTracker.class),
                new LineClickListener(),
                new TickClock()));
    }

}
