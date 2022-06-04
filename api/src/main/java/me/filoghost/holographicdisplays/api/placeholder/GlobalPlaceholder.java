/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

import org.jetbrains.annotations.Nullable;

/**
 * A global placeholder that displays the same text to all players. See {@link Placeholder} for general information
 * about placeholders.
 *
 * @since 1
 */
public interface GlobalPlaceholder extends Placeholder {

    /**
     * Callback for providing a placeholder replacement for a given optional argument. The same replacement is shown to
     * all players.
     * <p>
     * The argument is provided through the placeholder text: for example, the argument of {test} is null, the argument
     * of {test: hello world} is the string "hello world".
     * <p>
     * <b>Warning</b>: the implementation should be performance efficient, as it may be invoked often depending on the
     * refresh interval of the placeholder.
     *
     * @param argument the optional placeholder argument, null if not specified
     * @return the optional placeholder replacement, null to leave the placeholder unreplaced
     * @since 1
     */
    @Nullable String getReplacement(@Nullable String argument);

}
