/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import org.jetbrains.annotations.Nullable;

/**
 * A hologram line that can be clicked (left or right click).
 *
 * @since 1
 */
public interface ClickableLine extends HologramLine {

    /**
     * Sets the click listener.
     *
     * @param clickListener the new click listener
     * @since 1
     */
    void setClickListener(@Nullable ClickListener clickListener);

    /**
     * Returns the current click listener.
     *
     * @return the current click listener
     * @since 1
     */
    @Nullable ClickListener getClickListener();

}
