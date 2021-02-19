/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.mapped.ConfigPath;
import me.filoghost.fcommons.config.mapped.MappedConfig;

import java.util.Arrays;
import java.util.List;

public class MainConfigModel implements MappedConfig {
    
    @ConfigPath("space-between-lines")
    double spaceBetweenLines = 0.02;
    
    @ConfigPath("quick-edit-commands")
    boolean quickEditCommands = true;
    
    @ConfigPath("images.symbol")
    String imageSymbol = "[x]";
    
    @ConfigPath("images.transparency.space")
    String transparencySymbol = " [|] ";
    
    @ConfigPath("images.transparency.color")
    String transparencyColor = "&7";

    @ConfigPath("bungee.refresh-seconds")
    int bungeeRefreshSeconds = 3;

    @ConfigPath("bungee.use-RedisBungee")
    boolean useRedisBungee = false;

    @ConfigPath("bungee.pinger.enable")
    boolean pingerEnable = false;

    @ConfigPath("bungee.pinger.timeout")
    int pingerTimeout = 500;

    @ConfigPath("bungee.pinger.offline-motd")
    String pingerOfflineMotd = "&cOffline, couldn't get the MOTD";

    @ConfigPath("bungee.pinger.status.online")
    String pingerStatusOnline = "&aOnline";

    @ConfigPath("bungee.pinger.status.offline")
    String pingerStatusOffline = "&cOffline";

    @ConfigPath("bungee.pinger.motd-remove-leading-trailing-spaces")
    boolean pingerTrimMotd = true;

    @ConfigPath("bungee.pinger.servers")
    List<String> pingerServers = Arrays.asList("hub: 127.0.0.1:25565", "games: 127.0.0.1:25566");

    @ConfigPath("time.format")
    String timeFormat = "H:mm";

    @ConfigPath("time.zone")
    String timeZone = "GMT+1";
    
    @ConfigPath("update-notification")
    boolean updateNotification = true;

    @ConfigPath("debug")
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
                "precise-hologram-movement"
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
