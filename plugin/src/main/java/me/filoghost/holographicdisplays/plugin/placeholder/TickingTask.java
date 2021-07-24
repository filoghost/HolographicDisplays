/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;

public class TickingTask implements Runnable {

    private final TickClock tickClock;
    private final LineTrackerManager lineTrackerManager;

    private long lastErrorLogTick;

    public TickingTask(TickClock tickClock, LineTrackerManager lineTrackerManager) {
        this.tickClock = tickClock;
        this.lineTrackerManager = lineTrackerManager;
    }

    @Override
    public void run() {
        tickClock.incrementTick();

        try {
            lineTrackerManager.updateTrackersAndSendChanges();
        } catch (Throwable t) {
            // Avoid spamming the console, log the error at most once every 20 ticks
            if (tickClock.getCurrentTick() - lastErrorLogTick >= 20) {
                lastErrorLogTick = tickClock.getCurrentTick();
                Log.severe("Error while ticking holograms", t);
            }
        }
    }

}
