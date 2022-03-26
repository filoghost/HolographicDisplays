/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.Position;
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholderFactory;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholderReplacementSupplier;
import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.api.beta.placeholder.IndividualPlaceholderReplacementSupplier;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
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
        Preconditions.checkMainThread("async hologram creation");

        return apiHologramManager.createHologram(ImmutablePosition.of(location), plugin);
    }

    @Override
    public @NotNull Hologram createHologram(@NotNull Position position) {
        Preconditions.notNull(position, "position");
        Preconditions.notNull(position.getWorldName(), "position.getWorldName()");
        Preconditions.checkMainThread("async hologram creation");

        return apiHologramManager.createHologram(ImmutablePosition.of(position), plugin);
    }

    @Override
    public void registerGlobalPlaceholder(@NotNull String identifier, int refreshIntervalTicks, @NotNull GlobalPlaceholderReplacementSupplier replacementSupplier) {
        checkIdentifier(identifier);
        Preconditions.checkArgument(refreshIntervalTicks >= 0, "refreshIntervalTicks should be positive");
        Preconditions.notNull(replacementSupplier, "replacementSupplier");

        placeholderRegistry.registerGlobalPlaceholder(plugin, identifier, refreshIntervalTicks, replacementSupplier);
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
    public void registerIndividualPlaceholder(@NotNull String identifier, int refreshIntervalTicks, @NotNull IndividualPlaceholderReplacementSupplier replacementSupplier) {
        checkIdentifier(identifier);
        Preconditions.checkArgument(refreshIntervalTicks >= 0, "refreshIntervalTicks should be positive");
        Preconditions.notNull(replacementSupplier, "replacementSupplier");

        placeholderRegistry.registerIndividualPlaceholder(plugin, identifier, refreshIntervalTicks, replacementSupplier);
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
