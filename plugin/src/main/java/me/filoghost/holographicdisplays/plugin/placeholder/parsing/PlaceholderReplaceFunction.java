/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.parsing;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderReplaceFunction {

    PlaceholderReplaceFunction NO_REPLACEMENTS = placeholderOccurrence -> null;

    @Nullable String getReplacement(@NotNull PlaceholderOccurrence placeholderOccurrence);

}
