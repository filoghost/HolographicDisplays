/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.plugin.bridge.placeholderapi.PlaceholderAPIHook;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.PlaceholderTracker;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

class DisplayText {

    private final PlaceholderTracker placeholderTracker;

    private @Nullable StringWithPlaceholders unreplacedText;
    private boolean allowPlaceholders;
    private @Nullable String globalText;
    private boolean containsPlaceholderAPIPattern;
    private @Nullable Boolean containsIndividualPlaceholders;
    private long lastPlaceholderRegistryVersion;

    DisplayText(PlaceholderTracker placeholderTracker) {
        this.placeholderTracker = placeholderTracker;
    }

    boolean containsIndividualPlaceholders() {
        if (!allowPlaceholders || unreplacedText == null) {
            return false;
        }
        if (containsPlaceholderAPIPattern) {
            return true;
        }
        long currentPlaceholderRegistryVersion = placeholderTracker.getRegistryVersion();
        if (containsIndividualPlaceholders == null || lastPlaceholderRegistryVersion != currentPlaceholderRegistryVersion) {
            containsIndividualPlaceholders = placeholderTracker.containsIndividualPlaceholders(unreplacedText);
            lastPlaceholderRegistryVersion = currentPlaceholderRegistryVersion;
        }
        return containsIndividualPlaceholders;
    }

    void setUnreplacedText(@Nullable String text) {
        unreplacedText = text != null ? StringWithPlaceholders.of(text) : null;
        globalText = null;
        containsPlaceholderAPIPattern = unreplacedText != null
                && unreplacedText.anyLiteralPartMatch(PlaceholderAPIHook::containsPlaceholderPattern);
        containsIndividualPlaceholders = null;
    }

    @Nullable String getUnreplacedText() {
        return unreplacedText != null ? unreplacedText.getString() : null;
    }

    public boolean isAllowPlaceholders() {
        return allowPlaceholders;
    }

    public void setAllowPlaceholders(boolean allowPlaceholders) {
        this.allowPlaceholders = allowPlaceholders;
    }

    @Nullable String getGlobalText() {
        return globalText;
    }

    public boolean updateReplacements(Collection<TextLineViewer> viewers) {
        boolean changed = false;

        if (containsIndividualPlaceholders()) {
            for (TextLineViewer viewer : viewers) {
                String individualText = computeIndividualText(viewer);
                if (viewer.updateIndividualText(individualText)) {
                    changed = true;
                }
            }
        } else {
            String globalText = computeGlobalText();
            if (!Objects.equals(this.globalText, globalText)) {
                this.globalText = globalText;
                changed = true;
            }
        }

        return changed;
    }

    private @Nullable String computeGlobalText() {
        if (allowPlaceholders && unreplacedText != null && unreplacedText.containsPlaceholders()) {
            return unreplacedText.replacePlaceholders(placeholderTracker::updateAndGetGlobalReplacement);
        } else {
            return unreplacedText != null ? unreplacedText.getString() : null;
        }
    }

    public @NotNull String computeIndividualText(Viewer viewer) {
        Preconditions.notNull(unreplacedText, "unreplacedText");
        Player player = viewer.getPlayer();

        return unreplacedText.replaceParts(
                (PlaceholderOccurrence placeholderOccurrence) -> {
                    return placeholderTracker.updateAndGetReplacement(placeholderOccurrence, player);
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

}
