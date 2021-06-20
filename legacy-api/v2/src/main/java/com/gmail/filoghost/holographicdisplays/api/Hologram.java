/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holographicdisplays.api;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface Hologram {

    @Deprecated
    TextLine appendTextLine(String text);

    @Deprecated
    ItemLine appendItemLine(ItemStack itemStack);

    @Deprecated
    TextLine insertTextLine(int index, String text);

    @Deprecated
    ItemLine insertItemLine(int index, ItemStack itemStack);

    @Deprecated
    HologramLine getLine(int index);

    @Deprecated
    void removeLine(int index);

    @Deprecated
    void clearLines();

    @Deprecated
    int size();

    @Deprecated
    double getHeight();

    @Deprecated
    void teleport(Location location);

    @Deprecated
    void teleport(World world, double x, double y, double z);

    @Deprecated
    Location getLocation();

    @Deprecated
    double getX();

    @Deprecated
    double getY();

    @Deprecated
    double getZ();

    @Deprecated
    World getWorld();

    @Deprecated
    VisibilityManager getVisibilityManager();

    @Deprecated
    long getCreationTimestamp();

    @Deprecated
    boolean isAllowPlaceholders();

    @Deprecated
    void setAllowPlaceholders(boolean allowPlaceholders);

    @Deprecated
    void delete();

    @Deprecated
    boolean isDeleted();

}
