/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderExpansion;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import me.filoghost.holographicdisplays.plugin.tick.TickClock;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivePlaceholderTracker {

    private final PlaceholderRegistry registry;
    private final TickClock tickClock;
    private final PlaceholderExceptionHandler exceptionHandler;
    private final Map<PlaceholderOccurrence, ActivePlaceholder> activePlaceholders;

    private long lastRegistryVersion;

    public ActivePlaceholderTracker(PlaceholderRegistry registry, TickClock tickClock) {
        this.registry = registry;
        this.tickClock = tickClock;
        this.exceptionHandler = new PlaceholderExceptionHandler(tickClock);
        this.activePlaceholders = new HashMap<>();
    }

    public void clearOutdatedEntries() {
        long currentRegistryVersion = registry.getVersion();
        if (lastRegistryVersion == currentRegistryVersion) {
            return;
        }
        lastRegistryVersion = currentRegistryVersion;

        // Remove entries whose placeholder expansion sources are outdated
        activePlaceholders.entrySet().removeIf(entry -> {
            PlaceholderOccurrence placeholderOccurrence = entry.getKey();
            PlaceholderExpansion currentSource = entry.getValue().getSource();
            PlaceholderExpansion newSource = registry.find(placeholderOccurrence);

            return !Objects.equals(currentSource, newSource);
        });
    }

    public void clearUnusedEntries() {
        long currentTick = tickClock.getCurrentTick();
        activePlaceholders.values().removeIf(
                activePlaceholder -> currentTick - activePlaceholder.getLastRequestTick() >= 1);
    }

    public @Nullable String updateAndGetGlobalReplacement(PlaceholderOccurrence placeholderOccurrence) {
        try {
            ActivePlaceholder activePlaceholder = trackAndGetPlaceholder(placeholderOccurrence);
            if (activePlaceholder.isIndividual()) {
                return null;
            }
            return activePlaceholder.updateAndGetReplacement(null, tickClock.getCurrentTick());
        } catch (PlaceholderException e) {
            exceptionHandler.handle(e);
            return "[Error]";
        }
    }

    public @Nullable String updateAndGetReplacement(PlaceholderOccurrence placeholderOccurrence, Player player) {
        try {
            ActivePlaceholder activePlaceholder = trackAndGetPlaceholder(placeholderOccurrence);
            return activePlaceholder.updateAndGetReplacement(player, tickClock.getCurrentTick());
        } catch (PlaceholderException e) {
            exceptionHandler.handle(e);
            return "[Error]";
        }
    }

    private @NotNull ActivePlaceholder trackAndGetPlaceholder(PlaceholderOccurrence placeholderOccurrence) throws PlaceholderException {
        ActivePlaceholder activePlaceholder = activePlaceholders.get(placeholderOccurrence);

        if (activePlaceholder == null) {
            activePlaceholder = createActivePlaceholder(placeholderOccurrence);
            activePlaceholders.put(placeholderOccurrence, activePlaceholder);
        }

        return activePlaceholder;
    }

    private ActivePlaceholder createActivePlaceholder(PlaceholderOccurrence placeholderOccurrence) throws PlaceholderException {
        PlaceholderExpansion placeholderExpansion = registry.find(placeholderOccurrence);
        StandardPlaceholder placeholder;

        if (placeholderExpansion != null) {
            placeholder = placeholderExpansion.createPlaceholder(placeholderOccurrence.getArgument());
        } else {
            placeholder = null;
        }

        if (placeholder == null) {
            return new NullActivePlaceholder(placeholderExpansion);
        } else if (placeholder.isIndividual()) {
            return new IndividualActivePlaceholder(placeholder, placeholderOccurrence);
        } else {
            return new GlobalActivePlaceholder(placeholder, placeholderOccurrence);
        }
    }

    public boolean containsIndividualPlaceholders(@NotNull StringWithPlaceholders stringWithPlaceholders) {
        return stringWithPlaceholders.anyPlaceholderMatch(occurrence -> {
            PlaceholderExpansion placeholderExpansion = registry.find(occurrence);
            return placeholderExpansion != null && placeholderExpansion.isIndividual();
        });
    }

    public long getRegistryVersion() {
        return registry.getVersion();
    }

}
