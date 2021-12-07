/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.parsing;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderOccurrence;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderReplaceFunction {

    PlaceholderReplaceFunction NO_REPLACEMENTS = (player, placeholderOccurrence) -> null;

    @Nullable String getReplacement(@Nullable Player player, @NotNull PlaceholderOccurrence placeholderOccurrence);

}
