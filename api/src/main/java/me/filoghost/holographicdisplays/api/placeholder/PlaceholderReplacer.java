/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

import org.jetbrains.annotations.Nullable;

/**
 * Simple callback to provide a placeholder replacement.
 * 
 * @since 1
 */
@FunctionalInterface
public interface PlaceholderReplacer {

    /**
     * Callback for providing a placeholder replacement, given the argument of the placeholder (if present).
     * <p>
     * For example, the argument of {test} is null, the argument of {test: hello world} is the string "hello world".
     * <p>
     * <b>Warning</b>: this method should be performance efficient, as it may be invoked often.
     *
     * @return the placeholder replacement
     * @since 1
     */
    @Nullable
    String getReplacement(@Nullable String argument);

}
