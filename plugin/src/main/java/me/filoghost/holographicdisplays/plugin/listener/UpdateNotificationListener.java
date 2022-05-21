/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.plugin.Permissions;
import me.filoghost.holographicdisplays.plugin.config.Settings;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNotificationListener implements Listener {

    private final String newVersion;

    public UpdateNotificationListener(String newVersion) {
        this.newVersion = newVersion;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Settings.updateNotification) {
            Player player = event.getPlayer();

            if (player.hasPermission(Permissions.UPDATE_NOTIFICATION)) {
                player.sendMessage(ColorScheme.PRIMARY_DARK + "[HolographicDisplays] "
                        + ColorScheme.PRIMARY + "Found an update: " + newVersion + ". Download:");
                player.sendMessage(ColorScheme.PRIMARY_DARK + ">> "
                        + ColorScheme.PRIMARY + "https://dev.bukkit.org/projects/holographic-displays");
            }
        }
    }

}
