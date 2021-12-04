/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.beta.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1
 */
public interface IndividualPlaceholder extends Placeholder {

    /**
     * @since 1
     */
    @Nullable String getReplacement(@NotNull Player player, @Nullable String argument);

}
