/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderFactory;
import me.filoghost.holographicdisplays.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderExpansion;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class PlaceholdersReplacementTracker {

    private final PlaceholderRegistry registry;
    private final Map<PlaceholderOccurrence, ReplacementHolder> currentReplacements;

    public PlaceholdersReplacementTracker(PlaceholderRegistry registry) {
        this.registry = registry;
        // Use WeakHashMap to ensure that when a PlaceholderOccurrence is no longer referenced in other objects
        // the corresponding entry is removed from the map automatically.
        this.currentReplacements = new WeakHashMap<>();
    }

    public void clearOutdatedSources() {
        // Remove entries whose placeholder expansion sources are outdated
        currentReplacements.entrySet().removeIf(entry -> {
            PlaceholderOccurrence placeholderOccurrence = entry.getKey();
            PlaceholderExpansion currentSource = entry.getValue().source;
            PlaceholderExpansion newSource = registry.findBestMatch(placeholderOccurrence);

            return !Objects.equals(currentSource, newSource);
        });
    }
    
    public @Nullable String getOrUpdateReplacement(PlaceholderOccurrence placeholderOccurrence, long currentTick) throws PlaceholderException {
        ReplacementHolder replacementHolder = currentReplacements.get(placeholderOccurrence);
        
        if (replacementHolder == null) {
            replacementHolder = createReplacementHolder(placeholderOccurrence);
            currentReplacements.put(placeholderOccurrence, replacementHolder);
        }
        
        try {
            replacementHolder.refreshIfNeeded(placeholderOccurrence, currentTick);
        } catch (Throwable t) {
            throw new PlaceholderException(t, replacementHolder.source);
        }
        
        return replacementHolder.currentReplacement;        
    }

    private ReplacementHolder createReplacementHolder(PlaceholderOccurrence placeholderOccurrence) throws PlaceholderException {
        PlaceholderExpansion placeholderExpansion = registry.findBestMatch(placeholderOccurrence);
        Placeholder placeholder;
        
        if (placeholderExpansion != null) {
            PlaceholderFactory placeholderFactory = placeholderExpansion.getPlaceholderFactory();
            try {
                placeholder = placeholderFactory.getPlaceholder(placeholderOccurrence.getArgument());
            } catch (Throwable t) {
                throw new PlaceholderException(t, placeholderExpansion);
            }
        } else {
            placeholder = null;
        }
        
        return new ReplacementHolder(placeholderExpansion, placeholder);
    }
    
    
    private static class ReplacementHolder {
        
        final PlaceholderExpansion source;
        final Placeholder placeholder;
        
        String currentReplacement;
        long lastUpdateTick = -1;

        public ReplacementHolder(PlaceholderExpansion source, Placeholder placeholder) {
            this.source = source;
            this.placeholder = placeholder;
        }
        
        public void refreshIfNeeded(PlaceholderOccurrence placeholderOccurrence, long currentTick) {
            if (needsRefresh(currentTick)) {
                currentReplacement = placeholder.getReplacement(placeholderOccurrence.getArgument());
                lastUpdateTick = currentTick;
            }
        }

        private boolean needsRefresh(long currentTick) {
            if (placeholder == null || lastUpdateTick == currentTick) {
                return false; // No need to refresh
            }
            
            if (lastUpdateTick == -1) {
                return true; // Force at least the initial refresh
            }
            
            return currentTick - lastUpdateTick >= placeholder.getRefreshIntervalTicks();
        }

    }

}
