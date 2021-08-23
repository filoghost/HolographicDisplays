/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.current;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.HologramPosition;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
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
        Preconditions.notNull(location.getWorld(), "location's world");
        Preconditions.checkMainThread("async hologram creation");

        return apiHologramManager.createHologram(new BaseHologramPosition(location), plugin);
    }

    @Override
    public @NotNull Hologram createHologram(@NotNull HologramPosition position) {
        Preconditions.notNull(position, "position");
        Preconditions.notNull(position.getWorldName(), "position world name");
        Preconditions.checkMainThread("async hologram creation");

        return apiHologramManager.createHologram(new BaseHologramPosition(position), plugin);
    }

    @Override
    public void registerPlaceholder(@NotNull String identifier, int refreshIntervalTicks, @NotNull PlaceholderReplacer replacer) {
        Preconditions.notEmpty(identifier, "identifier");
        for (char c : identifier.toCharArray()) {
            Preconditions.checkArgument(isValidIdentifierCharacter(c), "identifier contains invalid character '" + c + "'");
        }
        Preconditions.checkArgument(refreshIntervalTicks >= 0, "refreshIntervalTicks should be positive");
        Preconditions.notNull(replacer, "replacer");

        placeholderRegistry.registerGlobalPlaceholderReplacer(plugin, identifier, refreshIntervalTicks, replacer);
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
        return placeholderRegistry.getRegisteredIdentifiers(plugin);
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
