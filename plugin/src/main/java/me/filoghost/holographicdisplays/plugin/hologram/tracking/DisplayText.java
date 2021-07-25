/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.holographicdisplays.plugin.bridge.placeholderapi.PlaceholderAPIHook;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class DisplayText {

    private final PlaceholderTracker placeholderTracker;
    private @NotNull StringWithPlaceholders textWithoutReplacements;
    private @NotNull StringWithPlaceholders textWithGlobalReplacements;

    DisplayText(PlaceholderTracker placeholderTracker) {
        this.placeholderTracker = placeholderTracker;
        this.textWithoutReplacements = StringWithPlaceholders.of(null);
        this.textWithGlobalReplacements = StringWithPlaceholders.of(null);
    }

    void setWithoutReplacements(@Nullable String textString) {
        textWithoutReplacements = StringWithPlaceholders.of(textString);
        textWithGlobalReplacements = textWithoutReplacements;
    }

    String getWithoutReplacements() {
        return textWithoutReplacements.getUnreplacedString();
    }

    String getWithGlobalReplacements() {
        return textWithGlobalReplacements.getUnreplacedString();
    }

    String getWithIndividualReplacements(Player player) {
        String textWithIndividualReplacements = textWithGlobalReplacements.replacePlaceholders((PlaceholderOccurrence occurrence) ->
                placeholderTracker.updateAndGetIndividualReplacement(occurrence, player));

        if (PlaceholderAPIHook.isEnabled() && PlaceholderAPIHook.containsPlaceholders(textWithIndividualReplacements)) {
            textWithIndividualReplacements = PlaceholderAPIHook.replacePlaceholders(player, textWithIndividualReplacements);
        }

        return textWithIndividualReplacements;
    }

    boolean updateGlobalReplacements() {
        if (!textWithoutReplacements.containsPlaceholders()) {
            return false;
        }

        StringWithPlaceholders textWithGlobalReplacements =
                textWithoutReplacements.partiallyReplacePlaceholders(placeholderTracker::updateAndGetGlobalReplacement);

        if (Objects.equals(this.textWithGlobalReplacements, textWithGlobalReplacements)) {
            return false;
        }

        this.textWithGlobalReplacements = textWithGlobalReplacements;
        return true;
    }

    boolean containsIndividualPlaceholders() {
        return placeholderTracker.containsIndividualPlaceholders(textWithoutReplacements)
                || PlaceholderAPIHook.containsPlaceholders(textWithoutReplacements.getUnreplacedString());
    }

}
