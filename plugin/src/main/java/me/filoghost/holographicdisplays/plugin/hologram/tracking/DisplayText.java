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
    private boolean containsPlaceholderAPIPattern;

    DisplayText(PlaceholderTracker placeholderTracker) {
        this.placeholderTracker = placeholderTracker;
        this.textWithoutReplacements = StringWithPlaceholders.of(null);
        this.textWithGlobalReplacements = textWithoutReplacements;
    }

    void setWithoutReplacements(@Nullable String textString) {
        textWithoutReplacements = StringWithPlaceholders.of(textString);
        textWithGlobalReplacements = textWithoutReplacements;
        containsPlaceholderAPIPattern = textWithoutReplacements.anyLiteralPartMatch(PlaceholderAPIHook::containsPlaceholderPattern);
    }

    String getWithoutReplacements() {
        return textWithoutReplacements.getString();
    }

    String getWithGlobalReplacements() {
        return textWithGlobalReplacements.getString();
    }

    String getWithIndividualReplacements(Player player) {
        return textWithGlobalReplacements.replaceParts(
                (PlaceholderOccurrence placeholderOccurrence) -> {
                    return placeholderTracker.updateAndGetIndividualReplacement(placeholderOccurrence, player);
                },
                (String literalPart) -> {
                    if (containsPlaceholderAPIPattern
                            && PlaceholderAPIHook.isEnabled()
                            && PlaceholderAPIHook.containsPlaceholderPattern(literalPart)) {
                        return PlaceholderAPIHook.replacePlaceholders(player, literalPart);
                    } else {
                        return literalPart;
                    }
                }
        );
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
        return containsPlaceholderAPIPattern || placeholderTracker.containsIndividualPlaceholders(textWithoutReplacements);
    }

}
