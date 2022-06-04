/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An individual placeholder that can display a different text to each player. See {@link Placeholder} for general
 * information about placeholders.
 *
 * @since 1
 */
public interface IndividualPlaceholder extends Placeholder {

    /**
     * Callback for providing a placeholder replacement for a given player and an optional argument. A different
     * replacement can be shown to each player.
     * <p>
     * The argument is provided through the placeholder text: for example, the argument of {test} is null, the argument
     * of {test: hello world} is the string "hello world".
     * <p>
     * <b>Warning</b>: the implementation should be performance efficient, as it may be invoked often depending on the
     * refresh interval of the placeholder and the number of players viewing the hologram.
     *
     * @param player the player that will see the provided replacement
     * @param argument the optional placeholder argument, null if not specified
     * @return the optional placeholder replacement, null to leave the placeholder unreplaced
     * @since 1
     */
    @Nullable String getReplacement(@NotNull Player player, @Nullable String argument);

}
