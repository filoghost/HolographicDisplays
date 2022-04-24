/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.beta.placeholder;

import org.jetbrains.annotations.Nullable;

/**
 * Simple callback to provide a placeholder replacement.
 *
 * @since 1
 */
@FunctionalInterface
public interface GlobalPlaceholderReplaceFunction {

    /**
     * @see GlobalPlaceholder#getReplacement(String)
     *
     * @since 1
     */
    @Nullable String getReplacement(@Nullable String argument);

}
