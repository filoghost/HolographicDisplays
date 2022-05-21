/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.test;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class Mocks {

    private static final Logger SERVER_LOGGER;
    public static final Server SERVER;
    public static final Plugin PLUGIN;

    static {
        SERVER_LOGGER = mock(Logger.class);
        SERVER = mock(Server.class);
        when(SERVER.getLogger()).thenReturn(SERVER_LOGGER);
        PLUGIN = mock(Plugin.class);
        when(PLUGIN.getName()).thenReturn("HolographicDisplays");
    }

    public static void prepareEnvironment() {
        if (Bukkit.getServer() == null) {
            Bukkit.setServer(Mocks.SERVER);
        }
    }

}
