/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.test;

import me.filoghost.holographicdisplays.core.api.v2.V2HologramManager;
import me.filoghost.holographicdisplays.core.listener.LineClickListener;
import me.filoghost.holographicdisplays.core.placeholder.tracking.ActivePlaceholderTracker;
import me.filoghost.holographicdisplays.core.tick.TickClock;
import me.filoghost.holographicdisplays.core.tracking.LineTrackerManager;

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
