/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1
 */
public interface ItemHologramLine extends ClickableHologramLine {

    /**
     * Returns the ItemStack of this ItemLine.
     *
     * @return the ItemStack if this ItemLine.
     * @since 1
     */
    @Nullable ItemStack getItemStack();

    /**
     * Sets the ItemStack for this ItemLine.
     *
     * @param itemStack the new item, should not be null.
     * @since 1
     */
    void setItemStack(@Nullable ItemStack itemStack);

    /**
     * Sets the pickup listener.
     *
     * @param pickupListener the new pickup listener, null to unset
     * @since 1
     */
    void setPickupListener(@Nullable PickupListener pickupListener);

    /**
     * Returns the current pickup listener.
     *
     * @return the current pickup listener, null if not present
     * @since 1
     */
    @Nullable PickupListener getPickupListener();

}
