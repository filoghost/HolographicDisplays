/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineClickListener;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;

public class TickingTask implements Runnable {

    private final TickClock tickClock;
    private final LineTrackerManager lineTrackerManager;
    private final LineClickListener lineClickListener;

    private long lastErrorLogTick;

    public TickingTask(TickClock tickClock, LineTrackerManager lineTrackerManager, LineClickListener lineClickListener) {
        this.tickClock = tickClock;
        this.lineTrackerManager = lineTrackerManager;
        this.lineClickListener = lineClickListener;
    }

    @Override
    public void run() {
        tickClock.incrementTick();

        try {
            lineTrackerManager.update();
        } catch (Throwable t) {
            // Catch all types of Throwable because we're using NMS code
            if (tickClock.getCurrentTick() - lastErrorLogTick >= 20) {
                // Avoid spamming the console, log the error at most once every 20 ticks
                lastErrorLogTick = tickClock.getCurrentTick();
                Log.severe("Error while ticking holograms", t);
            }
        }

        lineClickListener.processQueuedClickEvents();
    }

}
