/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.beta.hologram.line;

import org.jetbrains.annotations.Nullable;

/**
 * A hologram line that can be clicked (left or right click).
 *
 * @since 1
 */
public interface ClickableHologramLine extends HologramLine {

    /**
     * Returns the current click listener.
     *
     * @return the current click listener, null if not present
     * @since 1
     */
    @Nullable HologramLineClickListener getClickListener();

    /**
     * Sets the click listener.
     *
     * @param clickListener the new click listener, null to unset
     * @since 1
     */
    void setClickListener(@Nullable HologramLineClickListener clickListener);

}
