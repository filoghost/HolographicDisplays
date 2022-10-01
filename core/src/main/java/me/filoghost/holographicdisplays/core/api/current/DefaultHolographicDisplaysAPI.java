/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderReplaceFunction;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderReplaceFunction;
import me.filoghost.holographicdisplays.core.base.ImmutablePosition;
import me.filoghost.holographicdisplays.core.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

class DefaultHolographicDisplaysAPI implements HolographicDisplaysAPI {

    private final Plugin plugin;
    private final APIHologramManager apiHologramManager;
    private final PlaceholderRegistry placeholderRegistry;

    DefaultHolographicDisplaysAPI(Plugin plugin, APIHologramManager apiHologramManager, PlaceholderRegistry placeholderRegistry) {
        this.plugin = plugin;
        this.apiHologramManager = apiHologramManager;
        this.placeholderRegistry = placeholderRegistry;
    }

    @Override
    public @NotNull Hologram createHologram(@NotNull Location location) {
        Preconditions.notNull(location, "location");
        Preconditions.notNull(location.getWorld(), "location.getWorld()");

        return apiHologramManager.createHologram(ImmutablePosition.of(location), plugin);
    }

    @Override
    public @NotNull Hologram createHologram(@NotNull Position position) {
        Preconditions.notNull(position, "position");
        Preconditions.notNull(position.getWorldName(), "position.getWorldName()");

        return apiHologramManager.createHologram(ImmutablePosition.of(position), plugin);
    }

    @Override
    public void registerGlobalPlaceholder(@NotNull String identifier, int refreshIntervalTicks, @NotNull GlobalPlaceholderReplaceFunction replaceFunction) {
        checkIdentifier(identifier);
        Preconditions.checkArgument(refreshIntervalTicks >= 0, "refreshIntervalTicks should be positive");
        Preconditions.notNull(replaceFunction, "replaceFunction");

        placeholderRegistry.registerGlobalPlaceholder(plugin, identifier, refreshIntervalTicks, replaceFunction);
    }

    @Override
    public void registerGlobalPlaceholder(@NotNull String identifier, @NotNull GlobalPlaceholder placeholder) {
        checkIdentifier(identifier);
        Preconditions.notNull(placeholder, "placeholder");

        placeholderRegistry.registerGlobalPlaceholder(plugin, identifier, placeholder);
    }

    @Override
    public void registerGlobalPlaceholderFactory(@NotNull String identifier, @NotNull GlobalPlaceholderFactory placeholderFactory) {
        checkIdentifier(identifier);
        Preconditions.notNull(placeholderFactory, "placeholderFactory");

        placeholderRegistry.registerGlobalPlaceholderFactory(plugin, identifier, placeholderFactory);
    }

    @Override
    public void registerIndividualPlaceholder(@NotNull String identifier, int refreshIntervalTicks, @NotNull IndividualPlaceholderReplaceFunction replaceFunction) {
        checkIdentifier(identifier);
        Preconditions.checkArgument(refreshIntervalTicks >= 0, "refreshIntervalTicks should be positive");
        Preconditions.notNull(replaceFunction, "replaceFunction");

        placeholderRegistry.registerIndividualPlaceholder(plugin, identifier, refreshIntervalTicks, replaceFunction);
    }

    @Override
    public void registerIndividualPlaceholder(@NotNull String identifier, @NotNull IndividualPlaceholder placeholder) {
        checkIdentifier(identifier);
        Preconditions.notNull(placeholder, "placeholder");

        placeholderRegistry.registerIndividualPlaceholder(plugin, identifier, placeholder);
    }

    @Override
    public void registerIndividualPlaceholderFactory(@NotNull String identifier, @NotNull IndividualPlaceholderFactory placeholderFactory) {
        checkIdentifier(identifier);
        Preconditions.notNull(placeholderFactory, "placeholderFactory");

        placeholderRegistry.registerIndividualPlaceholderFactory(plugin, identifier, placeholderFactory);
    }

    private void checkIdentifier(String identifier) {
        Preconditions.notEmpty(identifier, "identifier");
        for (char c : identifier.toCharArray()) {
            Preconditions.checkArgument(isValidIdentifierCharacter(c), "identifier contains invalid character '" + c + "'");
        }
    }

    private boolean isValidIdentifierCharacter(char c) {
        return ('a' <= c && c <= 'z')
                || ('A' <= c && c <= 'Z')
                || ('0' <= c && c <= '9')
                || c == '-'
                || c == '_';
    }

    @Override
    public boolean isRegisteredPlaceholder(@NotNull String identifier) {
        Preconditions.notNull(identifier, "identifier");

        return placeholderRegistry.isRegisteredIdentifier(plugin, identifier);
    }

    @Override
    public @NotNull Collection<Hologram> getHolograms() {
        return apiHologramManager.getHologramsByPlugin(plugin);
    }

    @Override
    public void deleteHolograms() {
        apiHologramManager.deleteHologramsIf(apiHologram -> apiHologram.getCreatorPlugin().equals(plugin));
    }

    @Override
    public @NotNull Collection<String> getRegisteredPlaceholders() {
        return placeholderRegistry.getRegisteredPlaceholders(plugin);
    }

    @Override
    public void unregisterPlaceholder(@NotNull String identifier) {
        Preconditions.notNull(identifier, "identifier");

        placeholderRegistry.unregister(plugin, identifier);
    }

    @Override
    public void unregisterPlaceholders() {
        placeholderRegistry.unregisterAll(plugin);
    }

}
