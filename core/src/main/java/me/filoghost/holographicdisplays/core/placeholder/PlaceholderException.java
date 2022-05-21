/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder;

import me.filoghost.holographicdisplays.core.placeholder.registry.PlaceholderExpansion;

public class PlaceholderException extends Exception {

    private final PlaceholderExpansion placeholderExpansion;

    public PlaceholderException(Throwable cause, PlaceholderExpansion placeholderExpansion) {
        super(cause);
        this.placeholderExpansion = placeholderExpansion;
    }

    public PlaceholderExpansion getPlaceholderExpansion() {
        return placeholderExpansion;
    }

}
