/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.nms.common.NMSManager;
import me.filoghost.holographicdisplays.plugin.tick.TickingTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final NMSManager nmsManager;
    private final LineClickListener lineClickListener;
    private final TickingTask tickingTask;

    public PlayerListener(NMSManager nmsManager, LineClickListener lineClickListener, TickingTask tickingTask) {
        this.nmsManager = nmsManager;
        this.lineClickListener = lineClickListener;
        this.tickingTask = tickingTask;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        tickingTask.onPlayerJoin(player);
        nmsManager.injectPacketListener(player, lineClickListener);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        tickingTask.onPlayerQuit(player);
        nmsManager.uninjectPacketListener(player);
    }

}
