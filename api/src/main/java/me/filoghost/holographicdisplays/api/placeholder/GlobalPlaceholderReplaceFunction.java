/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.jetbrains.annotations.Nullable;

/**
 * Simplified version of {@link GlobalPlaceholder} where the refresh interval in ticks is passed through the
 * registration method
 * {@link HolographicDisplaysAPI#registerGlobalPlaceholder(String, int, GlobalPlaceholderReplaceFunction)} as a
 * constant.
 *
 * @since 1
 */
@FunctionalInterface
public interface GlobalPlaceholderReplaceFunction {

    /**
     * Same as {@link GlobalPlaceholder#getReplacement(String)}.
     *
     * @param argument the optional placeholder argument, null if not specified
     * @return the optional placeholder replacement, null to leave the placeholder unreplaced
     * @since 1
     */
    @Nullable String getReplacement(@Nullable String argument);

}
