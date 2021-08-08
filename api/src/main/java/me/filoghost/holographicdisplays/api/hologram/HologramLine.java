/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

import org.jetbrains.annotations.NotNull;

/**
 * Interface to represent a line in a Hologram.
 *
 * @since 1
 */
public interface HologramLine {

    /**
     * Returns the parent Hologram of this line.
     *
     * @return the parent Hologram.
     * @since 1
     */
    @NotNull Hologram getParent();

    /**
     * Removes this line from the parent Hologram.
     * Do not call if the Hologram has been deleted, an exception will be thrown.
     *
     * @since 1
     */
    void removeLine();

}
