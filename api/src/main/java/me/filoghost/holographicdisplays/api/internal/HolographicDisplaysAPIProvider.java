/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.internal;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class HolographicDisplaysAPIProvider {

    public static final String ERROR_IMPLEMENTATION_NOT_SET = "Holographic Displays did not load properly (no API implementation was set)";

    private static HolographicDisplaysAPIProvider implementation;

    public static void setImplementation(HolographicDisplaysAPIProvider implementation) {
        HolographicDisplaysAPIProvider.implementation = implementation;
    }

    public static HolographicDisplaysAPIProvider getImplementation() {
        if (implementation == null) {
            throw new IllegalStateException(ERROR_IMPLEMENTATION_NOT_SET);
        }

        return implementation;
    }

    public abstract HolographicDisplaysAPI getHolographicDisplaysAPI(Plugin plugin);

    public abstract Position createPosition(World world, double x, double y, double z);

    public abstract Position createPosition(String worldName, double x, double y, double z);

    public abstract Position createPosition(Location location);

}
