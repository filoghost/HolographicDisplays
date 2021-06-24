/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.HolographicDisplays;
import me.filoghost.holographicdisplays.plugin.Permissions;
import me.filoghost.holographicdisplays.plugin.disk.Settings;
import me.filoghost.updatechecker.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNotificationListener implements Listener {

    // The new version found by the updater, null if there is no new version
    private String newVersion;

    public void runAsyncUpdateCheck() {
        if (Settings.updateNotification) {
            UpdateChecker.run(HolographicDisplays.getInstance(), 75097, newVersion -> {
                this.newVersion = newVersion;
                Log.info("Found a new version available: " + newVersion);
                Log.info("Download it on Bukkit Dev:");
                Log.info("https://dev.bukkit.org/projects/holographic-displays");
            });
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Settings.updateNotification && newVersion != null) {
            Player player = event.getPlayer();

            if (player.hasPermission(Permissions.UPDATE_NOTIFICATION)) {
                player.sendMessage(ColorScheme.PRIMARY_DARKER + "[HolographicDisplays] "
                        + ColorScheme.PRIMARY + "Found an update: " + newVersion + ". Download:");
                player.sendMessage(ColorScheme.PRIMARY_DARKER + ">> "
                        + ColorScheme.PRIMARY + "https://dev.bukkit.org/projects/holographic-displays");
            }
        }
    }

}
