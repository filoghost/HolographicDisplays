/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

public class ServerAddress {
    
    private String name;
    private String ip;
    private int port;
    
    public ServerAddress(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return ip;
    }

    public int getPort() {
        return port;
    }
    
    @Override
    public String toString() {
        return name + " (" + ip + ":" + port + ")";
    }
    
}
