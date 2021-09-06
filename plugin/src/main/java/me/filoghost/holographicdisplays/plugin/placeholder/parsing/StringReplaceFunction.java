/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.parsing;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface StringReplaceFunction {

    StringReplaceFunction NO_REPLACEMENTS = string -> string;

    @NotNull String getReplacement(@NotNull String string);

}
