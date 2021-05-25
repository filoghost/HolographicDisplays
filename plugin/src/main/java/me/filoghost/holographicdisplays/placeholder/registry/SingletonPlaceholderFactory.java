/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder.registry;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;

class SingletonPlaceholderFactory implements PlaceholderFactory {

    private final Placeholder placeholder;

    SingletonPlaceholderFactory(Placeholder placeholder) {
        Preconditions.notNull(placeholder, "placeholder");
        this.placeholder = placeholder;
    }

    @Override
    public Placeholder getPlaceholder(String argument) {
        return placeholder;
    }

}
