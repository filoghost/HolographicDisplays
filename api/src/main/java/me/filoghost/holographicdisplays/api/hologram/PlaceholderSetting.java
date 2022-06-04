/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.hologram;

/**
 * The option to enable or disable placeholders inside the text lines of hologram.
 *
 * @see Hologram#setPlaceholderSetting(PlaceholderSetting)
 * @since 1
 */
public enum PlaceholderSetting {

    /**
     * The default setting for placeholders (which is currently equivalent to {@link #DISABLE_ALL}).
     *
     * @since 1
     */
    DEFAULT,

    /**
     * Enable all placeholders.
     *
     * @since 1
     */
    ENABLE_ALL,

    /**
     * Disable all placeholders.
     *
     * @since 1
     */
    DISABLE_ALL

}
