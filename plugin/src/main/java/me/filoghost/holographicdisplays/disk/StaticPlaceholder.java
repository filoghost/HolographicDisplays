/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

public class StaticPlaceholder {

    private final String identifier;
    private final String replacement;

    public StaticPlaceholder(String identifier, String replacement) {
        this.identifier = identifier;
        this.replacement = replacement;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getReplacement() {
        return replacement;
    }

}
