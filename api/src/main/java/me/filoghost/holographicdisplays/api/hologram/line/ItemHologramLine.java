/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * A hologram line displaying an item.
 *
 * @since 1
 */
public interface ItemHologramLine extends ClickableHologramLine {

    /**
     * Returns the currently displayed item.
     *
     * @return the current item
     * @since 1
     */
    @Nullable ItemStack getItemStack();

    /**
     * Sets the displayed item.
     *
     * @param itemStack the new item, null to display air
     * @since 1
     */
    void setItemStack(@Nullable ItemStack itemStack);

    /**
     * Returns the current pickup listener.
     *
     * @return the current pickup listener, null if absent
     * @since 1
     */
    @Nullable HologramLinePickupListener getPickupListener();

    /**
     * Sets the pickup listener.
     *
     * @param pickupListener the new pickup listener, null to remove it
     * @since 1
     */
    void setPickupListener(@Nullable HologramLinePickupListener pickupListener);

}
