/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.parsing;

import me.filoghost.fcommons.collection.CaseInsensitiveString;

public class PlaceholderIdentifier {

    private final CaseInsensitiveString identifier;
    
    public PlaceholderIdentifier(String identifier) {
        this.identifier = new CaseInsensitiveString(identifier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PlaceholderIdentifier other = (PlaceholderIdentifier) obj;
        return this.identifier.equals(other.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

}
