/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.test;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import static org.mockito.Mockito.*;

public class Mocks {

    public static final Plugin PLUGIN;
    public static final World WORLD;

    static {
        PLUGIN = mock(Plugin.class);
        when(PLUGIN.getName()).thenReturn("HolographicDisplays");
        WORLD = mock(World.class);
        when(WORLD.getName()).thenReturn("world");
    }
}
