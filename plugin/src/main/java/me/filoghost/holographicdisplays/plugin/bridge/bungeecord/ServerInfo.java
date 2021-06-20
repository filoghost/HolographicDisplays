/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.bridge.bungeecord;

import me.filoghost.fcommons.Strings;
import me.filoghost.holographicdisplays.plugin.disk.Configuration;

public class ServerInfo {
    
    private final boolean online;
    private final int onlinePlayers;
    private final int maxPlayers;
    private final String motdLine1, motdLine2;

    public static ServerInfo online(int onlinePlayers, int maxPlayers, String motd) {
        return new ServerInfo(true, onlinePlayers, maxPlayers, motd);
    }

    public static ServerInfo offline(String motd) {
        return new ServerInfo(false, 0, 0, motd);
    }

    private ServerInfo(boolean online, int onlinePlayers, int maxPlayers, String motd) {
        this.online = online;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        
        if (Strings.isEmpty(motd)) {
            motdLine1 = "";
            motdLine2 = "";
        } else if (motd.contains("\n")) {
            String[] lines = Strings.split(motd, "\n", 2);
            if (Configuration.pingerTrimMotd) {
                lines = Strings.trim(lines);
            }
            motdLine1 = lines[0];
            motdLine2 = lines.length > 1 ? lines[1] : "";
        } else {
            if (Configuration.pingerTrimMotd) {
                motd = motd.trim();
            }
            motdLine1 = motd;
            motdLine2 = "";
        }
    }

    public boolean isOnline() {
        return online;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getMotdLine1() {
        return motdLine1;
    }

    public String getMotdLine2() {
        return motdLine2;
    }

}
