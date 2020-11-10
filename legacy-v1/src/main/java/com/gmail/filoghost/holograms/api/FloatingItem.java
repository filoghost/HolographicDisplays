/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package com.gmail.filoghost.holograms.api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public interface FloatingItem {

    @Deprecated
    public boolean update();

    @Deprecated
    public void hide();

    @Deprecated
    public void setItemStack(ItemStack itemstack);

    @Deprecated
    public ItemStack getItemStack();

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
    public void teleport(Location location);

    @Deprecated
    public void setTouchHandler(ItemTouchHandler handler);

    @Deprecated
    public ItemTouchHandler getTouchHandler();

    @Deprecated
    public boolean hasTouchHandler();
    
    @Deprecated
    public void setPickupHandler(PickupHandler handler);

    @Deprecated
    public PickupHandler getPickupHandler();

    @Deprecated
    public boolean hasPickupHandler();

    @Deprecated
    public long getCreationTimestamp();

    @Deprecated
    public void delete();

    @Deprecated
    public boolean isDeleted();
}
