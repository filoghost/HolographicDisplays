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

class PlaceholderPart implements Part {

    private final PlaceholderOccurrence placeholderOccurrence;
    private final String unreplacedString;

    PlaceholderPart(@NotNull PlaceholderOccurrence placeholderOccurrence, @NotNull String unreplacedString) {
        this.placeholderOccurrence = placeholderOccurrence;
        this.unreplacedString = unreplacedString;
    }

    @NotNull String getValue(@Nullable Player player, PlaceholderReplaceFunction placeholderReplaceFunction) {
        String replacement = placeholderReplaceFunction.getReplacement(player, placeholderOccurrence);
        if (replacement != null) {
            return replacement;
        } else {
            // If no replacement is provided return the unreplaced placeholder string
            return unreplacedString;
        }
    }

    PlaceholderOccurrence getPlaceholderOccurrence() {
        return placeholderOccurrence;
    }

}
