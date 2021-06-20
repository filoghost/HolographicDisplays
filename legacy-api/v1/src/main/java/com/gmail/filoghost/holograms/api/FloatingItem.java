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
    boolean update();

    @Deprecated
    void hide();

    @Deprecated
    void setItemStack(ItemStack itemstack);

    @Deprecated
    ItemStack getItemStack();

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
    void teleport(Location location);

    @Deprecated
    void setTouchHandler(ItemTouchHandler handler);

    @Deprecated
    ItemTouchHandler getTouchHandler();

    @Deprecated
    boolean hasTouchHandler();

    @Deprecated
    void setPickupHandler(PickupHandler handler);

    @Deprecated
    PickupHandler getPickupHandler();

    @Deprecated
    boolean hasPickupHandler();

    @Deprecated
    long getCreationTimestamp();

    @Deprecated
    void delete();

    @Deprecated
    boolean isDeleted();

}
