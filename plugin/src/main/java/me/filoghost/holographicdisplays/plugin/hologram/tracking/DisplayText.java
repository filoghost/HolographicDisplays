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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class DisplayText {

    private final PlaceholderTracker placeholderTracker;
    private @Nullable StringWithPlaceholders textWithoutReplacements;
    private @Nullable StringWithPlaceholders textWithGlobalReplacements;

    DisplayText(PlaceholderTracker placeholderTracker) {
        this.placeholderTracker = placeholderTracker;
    }

    void set(@Nullable String textString) {
        textWithoutReplacements = StringWithPlaceholders.of(textString);
        textWithGlobalReplacements = null;
    }

    String get() {
        return textWithoutReplacements != null ? textWithoutReplacements.getUnreplacedString() : null;
    }

    boolean updateGlobalReplacements() {
        if (textWithoutReplacements == null || !textWithoutReplacements.containsPlaceholders()) {
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

    String getWithGlobalReplacements() {
        if (containsIndividualPlaceholders()) {
            throw new IllegalStateException("contains individual placeholders");
        }

        if (textWithoutReplacements == null || !textWithoutReplacements.containsPlaceholders()) {
            return textWithoutReplacements.getUnreplacedString();
        }

        return textWithGlobalReplacements.getUnreplacedString();
    }

    String getWithIndividualReplacements(Player player) {
        if (!containsIndividualPlaceholders()) {
            throw new IllegalStateException("does not contain individual placeholders");
        }

        if (textWithoutReplacements == null || !textWithoutReplacements.containsPlaceholders()) {
            return textWithoutReplacements.getUnreplacedString();
        }

        String text = textWithGlobalReplacements.replacePlaceholders((PlaceholderOccurrence occurrence) ->
                placeholderTracker.updateAndGetIndividualReplacement(occurrence, player));

        if (PlaceholderAPIHook.isEnabled() && PlaceholderAPIHook.containsPlaceholders(text)) {
            text = PlaceholderAPIHook.replacePlaceholders(player, text);
        }

        return text;
    }

    boolean containsIndividualPlaceholders() {
        if (textWithoutReplacements == null) {
            return false;
        }

        return placeholderTracker.containsIndividualPlaceholders(textWithoutReplacements)
                || PlaceholderAPIHook.containsPlaceholders(textWithoutReplacements.getUnreplacedString());
    }

}
