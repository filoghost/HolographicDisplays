/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.test;

import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;

import static org.mockito.Mockito.*;

public class TestAPIHologramsManager extends APIHologramManager {

    public TestAPIHologramsManager() {
        super(new LineTrackerManager(new TestNMSManager(), mock(PlaceholderTracker.class)));
    }

}
