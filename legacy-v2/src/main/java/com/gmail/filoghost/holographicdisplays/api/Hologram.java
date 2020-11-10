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
    public TextLine appendTextLine(String text);

    @Deprecated
    public ItemLine appendItemLine(ItemStack itemStack);
    
    @Deprecated
    public TextLine insertTextLine(int index, String text);

    @Deprecated
    public ItemLine insertItemLine(int index, ItemStack itemStack);

    @Deprecated
    public HologramLine getLine(int index);
    
    @Deprecated
    public void removeLine(int index);

    @Deprecated
    public void clearLines();

    @Deprecated
    public int size();

    @Deprecated
    public double getHeight();

    @Deprecated
    public void teleport(Location location);
    
    @Deprecated
    public void teleport(World world, double x, double y, double z);
    
    @Deprecated
    public Location getLocation();
    
    @Deprecated
    public double getX();

    @Deprecated
    public double getY();

    @Deprecated
    public double getZ();
    
    @Deprecated
    public World getWorld();

    @Deprecated
    public VisibilityManager getVisibilityManager();

    @Deprecated
    public long getCreationTimestamp();

    @Deprecated
    public boolean isAllowPlaceholders();

    @Deprecated
    public void setAllowPlaceholders(boolean allowPlaceholders);
    
    @Deprecated
    public void delete();
    
    @Deprecated
    public boolean isDeleted();
    
}
