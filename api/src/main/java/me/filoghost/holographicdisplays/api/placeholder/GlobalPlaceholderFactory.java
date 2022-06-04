/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

import org.jetbrains.annotations.Nullable;

/**
 * Advanced class to create separate {@link GlobalPlaceholder} instances depending on the placeholder argument, instead
 * of providing a single one when registering the placeholder.
 * <p>
 * This is mostly used for performance reasons, to parse the argument only when necessary, instead of doing it every
 * time the method {@link GlobalPlaceholder#getReplacement(String)} is invoked. For example, the argument might consist
 * of multiple comma-separated values, be a JSON string, or require a regular expression to parse it.
 * <p>
 * Another less common use case is to keep a separate internal state for each different argument.
 *
 * @since 1
 */
public interface GlobalPlaceholderFactory {

    /**
     * Returns the placeholder instance to provide the replacement for the given argument. This method might be invoked
     * again for the same argument if the placeholder is temporarily unused (not displayed to any player).
     *
     * @param argument the argument for which the placeholder instance will provide the replacement
     * @return the placeholder instance
     * @since 1
     */
    @Nullable GlobalPlaceholder getPlaceholder(@Nullable String argument);

}
