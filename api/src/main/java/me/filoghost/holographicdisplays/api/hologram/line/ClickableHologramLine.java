/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.jetbrains.annotations.Nullable;

/**
 * A hologram line that can be left-clicked or right-click. Currently, both {@link TextHologramLine} and
 * {@link ItemHologramLine} are clickable.
 *
 * @since 1
 */
public interface ClickableHologramLine extends HologramLine {

    /**
     * Returns the current click listener.
     *
     * @return the current click listener, null if absent
     * @since 1
     */
    @Nullable HologramLineClickListener getClickListener();

    /**
     * Sets the click listener.
     *
     * @param clickListener the new click listener, null to remove it
     * @since 1
     */
    void setClickListener(@Nullable HologramLineClickListener clickListener);

}
