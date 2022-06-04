/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram.line;

import org.jetbrains.annotations.NotNull;

/**
 * The listener class for {@link HologramLineClickEvent}.
 * <p>
 * This is not a Bukkit listener, it must be set with
 * {@link ClickableHologramLine#setClickListener(HologramLineClickListener)}.
 *
 * @since 1
 */
@FunctionalInterface
public interface HologramLineClickListener {

    /**
     * Invoked when a player clicks on the clickable line.
     *
     * @param clickEvent the event data
     * @since 1
     */
    void onClick(@NotNull HologramLineClickEvent clickEvent);

}
