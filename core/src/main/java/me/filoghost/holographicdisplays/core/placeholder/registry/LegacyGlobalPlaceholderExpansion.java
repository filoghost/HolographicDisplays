/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.placeholder.registry;

import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholderFactory;
import org.bukkit.plugin.Plugin;

public class LegacyGlobalPlaceholderExpansion extends GlobalPlaceholderExpansion {

    private final String textPlaceholder;

    LegacyGlobalPlaceholderExpansion(
            Plugin plugin,
            String identifier,
            GlobalPlaceholderFactory placeholderFactory,
            String textPlaceholder) {
        super(plugin, identifier, placeholderFactory);
        this.textPlaceholder = textPlaceholder;
    }

    public String getTextPlaceholder() {
        return textPlaceholder;
    }

}
