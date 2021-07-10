/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.disk;

import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.mapped.MappedConfig;
import me.filoghost.fcommons.config.mapped.Path;

import java.util.Arrays;
import java.util.List;

public class SettingsModel implements MappedConfig {

    @Path("space-between-lines")
    double spaceBetweenLines = 0.02;

    @Path("quick-edit-commands")
    boolean quickEditCommands = true;

    @Path("images.symbol")
    String imageSymbol = "[x]";

    @Path("images.transparency.space")
    String transparencySymbol = " [|] ";

    @Path("bungee.refresh-seconds")
    int bungeeRefreshSeconds = 3;

    @Path("bungee.use-RedisBungee")
    boolean useRedisBungee = false;

    @Path("bungee.pinger.enable")
    boolean pingerEnable = false;

    @Path("bungee.pinger.timeout")
    int pingerTimeout = 500;

    @Path("bungee.pinger.offline-motd")
    String pingerOfflineMotd = "&cOffline, couldn't get the MOTD";

    @Path("bungee.pinger.status.online")
    String pingerStatusOnline = "&aOnline";

    @Path("bungee.pinger.status.offline")
    String pingerStatusOffline = "&cOffline";

    @Path("bungee.pinger.motd-remove-leading-trailing-spaces")
    boolean pingerTrimMotd = true;

    @Path("bungee.pinger.servers")
    List<String> pingerServers = Arrays.asList("hub: 127.0.0.1:25565", "games: 127.0.0.1:25566");

    @Path("time.format")
    String timeFormat = "H:mm";

    @Path("time.zone")
    String timeZone = "GMT+1";

    @Path("update-notification")
    boolean updateNotification = true;

    @Path("debug")
    boolean debug = false;

    @Override
    public List<String> getHeader() {
        return Arrays.asList(
                "",
                "Plugin page: https://dev.bukkit.org/projects/holographic-displays",
                "",
                "Created by filoghost.",
                ""
        );
    }

    @Override
    public boolean beforeSave(Config rawConfig) {
        List<String> pathsToRemove = Arrays.asList(
                "vertical-spacing",
                "time-format",
                "bungee-refresh-seconds",
                "using-RedisBungee",
                "bungee-online-format",
                "bungee-offline-format",
                "precise-hologram-movement",
                "images.transparency.color"
        );

        boolean modified = false;

        for (String path : pathsToRemove) {
            if (rawConfig.get(path) != null) {
                rawConfig.remove(path);
                modified = true;
            }
        }

        return modified;
    }

}
