/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.beta.hologram.line;

import org.jetbrains.annotations.NotNull;

/**
 * Interface to handle clickable hologram lines.
 *
 * @since 1
 */
@FunctionalInterface
public interface HologramLineClickListener {

    /**
     * @since 1
     */
    void onClick(@NotNull HologramLineClickEvent clickEvent);

}
