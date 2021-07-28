/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTouchListener;
import me.filoghost.holographicdisplays.plugin.hologram.tracking.LineTrackerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final NMSManager nmsManager;
    private final LineTrackerManager lineTrackerManager;
    private final LineTouchListener lineTouchListener;

    public PlayerListener(NMSManager nmsManager, LineTrackerManager lineTrackerManager, LineTouchListener lineTouchListener) {
        this.nmsManager = nmsManager;
        this.lineTrackerManager = lineTrackerManager;
        this.lineTouchListener = lineTouchListener;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        nmsManager.injectPacketListener(player, lineTouchListener);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        lineTrackerManager.onPlayerQuit(player);
        nmsManager.uninjectPacketListener(player);
    }

}
