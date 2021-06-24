/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package me.filoghost.holographicdisplays.plugin.disk;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.common.DebugLogger;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import org.bukkit.ChatColor;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    public static double spaceBetweenLines;
    public static boolean quickEditCommands;
    public static DateTimeFormatter timeFormat;
    public static boolean updateNotification;

    public static String imageSymbol;
    public static String transparencySymbol;
    public static ChatColor transparencyColor;

    public static int bungeeRefreshSeconds;
    public static boolean useRedisBungee;
    public static boolean pingerEnabled;
    public static int pingerTimeout;
    public static String pingerOfflineMotd;
    public static String pingerStatusOnline;
    public static String pingerStatusOffline;
    public static boolean pingerTrimMotd;
    public static Map<String, ServerAddress> pingerServerAddresses;

    public static void load(ConfigurationFileModel config, ErrorCollector errorCollector) {
        spaceBetweenLines = config.spaceBetweenLines;
        quickEditCommands = config.quickEditCommands;
        timeFormat = parseTimeFormatter(config.timeFormat, config.timeZone, errorCollector);
        updateNotification = config.updateNotification;

        imageSymbol = DisplayFormat.apply(config.imageSymbol);
        transparencySymbol = DisplayFormat.apply(config.transparencySymbol);
        transparencyColor = parseTransparencyColor(config.transparencyColor, errorCollector);

        bungeeRefreshSeconds = parseBungeeRefreshInterval(config.bungeeRefreshSeconds, errorCollector);
        useRedisBungee = config.useRedisBungee;
        pingerEnabled = config.pingerEnable;
        pingerTimeout = parsePingerTimeout(config.pingerTimeout, errorCollector);
        pingerOfflineMotd = DisplayFormat.apply(config.pingerOfflineMotd);
        pingerStatusOnline = DisplayFormat.apply(config.pingerStatusOnline);
        pingerStatusOffline = DisplayFormat.apply(config.pingerStatusOffline);
        pingerTrimMotd = config.pingerTrimMotd;

        pingerServerAddresses = new HashMap<>();
        if (pingerEnabled) {
            for (String singleServer : config.pingerServers) {
                ServerAddress serverAddress = parseServerAddress(singleServer, errorCollector);
                if (serverAddress != null) {
                    pingerServerAddresses.put(serverAddress.getName(), serverAddress);
                }
            }
        }

        DebugLogger.setDebugEnabled(config.debug);
    }

    private static DateTimeFormatter parseTimeFormatter(String pattern, String timeZone, ErrorCollector errorCollector) {
        DateTimeFormatter timeFormat;

        try {
            timeFormat = DateTimeFormatter.ofPattern(pattern);
        } catch (IllegalArgumentException ex) {
            timeFormat = DateTimeFormatter.ofPattern("H:mm");
            errorCollector.add("time format not valid in the configuration, using the default");
        }

        try {
            timeFormat = timeFormat.withZone(ZoneId.of(timeZone));
        } catch (DateTimeException e) {
            errorCollector.add("time zone not valid in the configuration, using the default");
        }

        return timeFormat;
    }

    private static ChatColor parseTransparencyColor(String transparencyColor, ErrorCollector errorCollector) {
        transparencyColor = transparencyColor.replace('&', ChatColor.COLOR_CHAR);

        for (ChatColor chatColor : ChatColor.values()) {
            if (chatColor.toString().equals(transparencyColor)) {
                return chatColor;
            }
        }

        errorCollector.add("chat color for transparency in the configuration is not valid, light gray (&7) will be used");
        return ChatColor.GRAY;
    }

    private static int parseBungeeRefreshInterval(int interval, ErrorCollector errorCollector) {
        if (interval < 1) {
            errorCollector.add("the minimum interval for pinging BungeeCord's servers is 1 second. It has been automatically set");
            return 1;
        } else if (interval > 60) {
            errorCollector.add("the maximum interval for pinging BungeeCord's servers is 60 seconds. It has been automatically set");
            return 60;
        } else {
            return interval;
        }
    }

    private static int parsePingerTimeout(int timeout, ErrorCollector errorCollector) {
        if (timeout < 100) {
            errorCollector.add("the minimum timeout for pinging BungeeCord's servers is 100 milliseconds. It has been automatically set");
            return 100;
        } else if (timeout > 10000) {
            errorCollector.add("the maximum timeout for pinging BungeeCord's servers is 10000 milliseconds. It has been automatically set");
            return 10000;
        } else {
            return timeout;
        }
    }

    private static ServerAddress parseServerAddress(String singleServer, ErrorCollector errorCollector) {
        String[] nameAndAddress = Strings.splitAndTrim(singleServer, ":", 2);
        if (nameAndAddress.length < 2) {
            errorCollector.add("the server info \"" + singleServer + "\" is not valid."
                    + " There should be a name and an address, separated by a colon");
            return null;
        }

        String name = nameAndAddress[0];
        String address = nameAndAddress[1];

        String ip;
        int port;

        if (address.contains(":")) {
            String[] ipAndPort = Strings.splitAndTrim(address, ":", 2);
            ip = ipAndPort[0];
            try {
                port = Integer.parseInt(ipAndPort[1]);
            } catch (NumberFormatException e) {
                errorCollector.add("invalid port number in the server info \"" + singleServer + "\"");
                return null;
            }
        } else {
            ip = address;
            port = 25565; // Default Minecraft server port
        }

        return new ServerAddress(name, ip, port);
    }

}
