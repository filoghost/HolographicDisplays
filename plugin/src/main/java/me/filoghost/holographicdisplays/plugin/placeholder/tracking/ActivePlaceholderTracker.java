/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.placeholder.tracking;

import me.filoghost.holographicdisplays.plugin.placeholder.PlaceholderException;
import me.filoghost.holographicdisplays.plugin.placeholder.StandardPlaceholder;
import me.filoghost.holographicdisplays.plugin.tick.TickClock;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.PlaceholderOccurrence;
import me.filoghost.holographicdisplays.plugin.placeholder.parsing.StringWithPlaceholders;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderExpansion;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.WeakHashMap;

public class ActivePlaceholderTracker {

    private final PlaceholderRegistry registry;
    private final TickClock tickClock;
    private final PlaceholderExceptionHandler exceptionHandler;

    // Use WeakHashMap to ensure that when a PlaceholderOccurrence is no longer referenced in other objects
    // the corresponding entry is removed from the map automatically.
    private final WeakHashMap<PlaceholderOccurrence, ActivePlaceholder> activePlaceholders;

    private long lastRegistryVersion;

    public ActivePlaceholderTracker(PlaceholderRegistry registry, TickClock tickClock) {
        this.registry = registry;
        this.tickClock = tickClock;
        this.exceptionHandler = new PlaceholderExceptionHandler(tickClock);
        this.activePlaceholders = new WeakHashMap<>();
    }

    public void update() {
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
