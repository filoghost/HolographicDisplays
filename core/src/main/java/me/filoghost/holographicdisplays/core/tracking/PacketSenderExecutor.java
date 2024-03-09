/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.tracking;

import me.filoghost.fcommons.logging.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is a quick but ugly helper class for creating and sending packets async.
 * Static classes like this should be avoided.
 * Important note: this executor can only have one thread, as packets must be sent in a precise order.
 */
public class PacketSenderExecutor {

    private static volatile BlockingQueue<Runnable> tasks;

    private static final Runnable STOP_MARKER_TASK = () -> {};

    public static void execute(Runnable task) {
        tasks.add(task);
    }

    public static void start() {
        tasks = new LinkedBlockingQueue<>();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = tasks.take();
                    if (task == STOP_MARKER_TASK) {
                        return;
                    }
                    task.run();
                } catch (Throwable t) {
                    Log.severe("Error in packet sender task", t);
                }
            }
        });
        thread.setName("Holographic Displays async packets");
        thread.start();
    }

    public static void stopGracefully() {
        if (tasks != null) {
            tasks.add(STOP_MARKER_TASK);
        }
    }

}
