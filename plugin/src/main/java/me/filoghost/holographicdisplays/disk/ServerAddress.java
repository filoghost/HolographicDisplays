/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

public class ServerAddress {
    
    private String ip;
    private int port;
    
    public ServerAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getAddress() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }
    
}
