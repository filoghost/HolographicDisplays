/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.parsing;

import org.jetbrains.annotations.NotNull;

class StringPart implements Part {

    private final String value;

    StringPart(@NotNull String value) {
        this.value = value;
    }

    @NotNull String getValue(StringReplaceFunction stringReplaceFunction) {
        return stringReplaceFunction.getReplacement(value);
    }

}
