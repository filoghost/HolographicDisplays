/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import org.jetbrains.annotations.Nullable;

/**
 * A line of a Hologram that can be touched (right click).
 *
 * @since 1
 */
public interface TouchableLine extends HologramLine {

    /**
     * Sets the TouchHandler for this line.
     *
     * @param touchHandler the new TouchHandler, can be null.
     * @since 1
     */
    void setTouchHandler(@Nullable TouchHandler touchHandler);

    /**
     * Returns the current TouchHandler of this line.
     *
     * @return the current TouchHandler, can be null.
     * @since 1
     */
    @Nullable TouchHandler getTouchHandler();

}
