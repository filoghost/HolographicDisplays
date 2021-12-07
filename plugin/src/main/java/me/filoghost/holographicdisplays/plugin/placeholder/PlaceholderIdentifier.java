/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder;

import me.filoghost.fcommons.collection.CaseInsensitiveString;

public class PlaceholderIdentifier {

    private final CaseInsensitiveString identifier;

    public PlaceholderIdentifier(String identifier) {
        this.identifier = new CaseInsensitiveString(identifier);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PlaceholderIdentifier)) {
            return false;
        }

        PlaceholderIdentifier other = (PlaceholderIdentifier) obj;
        return this.identifier.equals(other.identifier);
    }

    @Override
    public final int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

}
