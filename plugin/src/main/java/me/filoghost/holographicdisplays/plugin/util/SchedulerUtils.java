/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.util;

import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import org.bukkit.Bukkit;

public class SchedulerUtils {
    
    public static void runOnMainThread(Runnable task) {
        if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(HolographicDisplays.getInstance(), task);
        }
    }

}
