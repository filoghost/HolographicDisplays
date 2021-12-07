/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.tracking;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.tracking.ActivePlaceholderTracker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

class DisplayText {

    private final ActivePlaceholderTracker placeholderTracker;

    private @Nullable StringWithPlaceholders unreplacedText;
    private boolean allowPlaceholders;
    private @Nullable String globalText;
    private @Nullable Boolean containsIndividualPlaceholders;
    private long lastPlaceholderRegistryVersion;

    DisplayText(ActivePlaceholderTracker placeholderTracker) {
        this.placeholderTracker = placeholderTracker;
    }

    boolean containsIndividualPlaceholders() {
        if (!allowPlaceholders || unreplacedText == null) {
            return false;
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
            this.globalText = null;
            for (TextLineViewer viewer : viewers) {
                if (viewer.updateIndividualText()) {
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
            return unreplacedText.replacePlaceholders(null, placeholderTracker);
        } else {
            return unreplacedText != null ? unreplacedText.getString() : null;
        }
    }

    public @NotNull String computeIndividualText(Viewer viewer) {
        Preconditions.notNull(unreplacedText, "unreplacedText");

        return unreplacedText.replacePlaceholders(viewer.getPlayer(), placeholderTracker);
    }

}
