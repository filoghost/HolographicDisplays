/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.placeholder.tracking.PlaceholderLineTracker;

public class TickingTask implements Runnable {

    private final TickClock tickClock;
    private final PlaceholderLineTracker placeholderLineTracker;

    public TickingTask(TickClock tickClock, PlaceholderLineTracker placeholderLineTracker) {
        this.tickClock = tickClock;
        this.placeholderLineTracker = placeholderLineTracker;
    }

    @Override
    public void run() {
        tickClock.incrementTick();
        placeholderLineTracker.updateEntitiesWithGlobalPlaceholders();
    }

}
