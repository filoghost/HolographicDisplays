/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.line;

import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import org.jetbrains.annotations.Nullable;

/**
 * A line of a Hologram that can be picked up.
 *
 * @since 1
 */
public interface CollectableLine extends HologramLine {
    
    /**
     * Sets the PickupHandler for this line.
     *
     * @param pickupHandler the new PickupHandler, can be null.
     * @since 1
     */
    void setPickupHandler(@Nullable PickupHandler pickupHandler);
    
    /**
     * Returns the current PickupHandler of this line.
     *
     * @return the current PickupHandler, can be null.
     * @since 1
     */
    @Nullable PickupHandler getPickupHandler();
    
}
