/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final LineTrackerManager lineTrackerManager;

    public PlayerQuitListener(LineTrackerManager lineTrackerManager) {
        this.lineTrackerManager = lineTrackerManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lineTrackerManager.onPlayerQuit(event.getPlayer());
    }

}
