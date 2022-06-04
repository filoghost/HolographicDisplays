/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api;

import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderReplaceFunction;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholder;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderFactory;
import me.filoghost.holographicdisplays.api.placeholder.IndividualPlaceholderReplaceFunction;
import me.filoghost.holographicdisplays.api.placeholder.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Main entry point for accessing the Holographic Displays API.
 * <p>
 * Each API instance only manages the holograms and the placeholders of a specific plugin, see {@link #get(Plugin)}.
 *
 * @since 1
 */
public interface HolographicDisplaysAPI {

    /**
     * Returns the API version number, which is increased every time the API changes.
     * <p>
     * All public API classes and methods are documented with the Javadoc tag {@code @since}, indicating in which API
     * version that element was introduced.
     * <p>
     * This number can be used it to require a minimum version, as features may be added (rarely removed) in future
     * versions. The first version of the API is 1.
     * <p>
     * The API version is unrelated to the standard plugin version.
     *
     * @return the current API version
     * @since 1
     */
    static int getVersion() {
        return 1;
    }

    /**
     * Returns the API instance for managing holograms and placeholders of the specified plugin.
     * <p>
     * Holograms and placeholders created by other plugins are completely separate, each API instance can only manage
     * and retrieve the ones of the specified plugin. Unless for very specific reasons, a plugin should only use its own
     * API instance.
     *
     * @param plugin the plugin using the API
     * @return an API instance for the specified plugin
     * @since 1
     */
    static @NotNull HolographicDisplaysAPI get(@NotNull Plugin plugin) {
        return HolographicDisplaysAPIProvider.getImplementation().getHolographicDisplaysAPI(plugin);
    }

    /**
     * Creates a hologram.
     *
     * @param location the initial location of the hologram
     * @return the created hologram
     * @since 1
     */
    @NotNull Hologram createHologram(@NotNull Location location);

    /**
     * Creates a hologram.
     *
     * @param position the initial position of the hologram
     * @return the created hologram
     * @since 1
     */
    @NotNull Hologram createHologram(@NotNull Position position);

    /**
     * Returns all the holograms. Deleted holograms are not included.
     *
     * @return an immutable copy of all the holograms
     * @since 1
     */
    @NotNull Collection<Hologram> getHolograms();

    /**
     * Deletes all the holograms. Equivalent to invoking {@link Hologram#delete()} on each hologram.
     *
     * @since 1
     */
    void deleteHolograms();

    /**
     * Registers a new global placeholder. Any previously registered element (global or individual) with the same
     * identifier is overwritten. See {@link GlobalPlaceholder} to know more about global placeholders.
     * <p>
     * This is a simplified method to register a placeholder by providing separately its replace function for
     * {@link GlobalPlaceholder#getReplacement(String)} and a fixed refresh interval for
     * {@link Placeholder#getRefreshIntervalTicks()}
     *
     * @param identifier the case-insensitive identifier of the placeholder
     * @param refreshIntervalTicks the minimum interval in ticks between invocations of the replace function
     *         (when the placeholder is in use), see {@link Placeholder#getRefreshIntervalTicks()}
     * @param replaceFunction the callback function to provide the replacement text to display
     * @since 1
     */
    void registerGlobalPlaceholder(
            @NotNull String identifier,
            int refreshIntervalTicks,
            @NotNull GlobalPlaceholderReplaceFunction replaceFunction);

    /**
     * Registers a new global placeholder. Any previously registered element (global or individual) with the same
     * identifier is overwritten. See {@link GlobalPlaceholder} to know more about global placeholders.
     *
     * @param identifier the case-insensitive identifier of the placeholder
     * @param placeholder the placeholder to register
     * @since 1
     */
    void registerGlobalPlaceholder(@NotNull String identifier, @NotNull GlobalPlaceholder placeholder);

    /**
     * Registers a new global placeholder factory. Any previously registered element (global or individual) with the
     * same identifier is overwritten. See {@link GlobalPlaceholder} to know more about global placeholders.
     * <p>
     * This method is more complex and not usually needed: it is necessary if parsing the argument of the placeholder is
     * performance intensive, for example a mathematical expression or a JSON string.
     * <p>
     * See {@link GlobalPlaceholderFactory} to know more about global placeholder factories.
     *
     * @param identifier the case-insensitive identifier of the placeholder factory
     * @param placeholderFactory the placeholder factory to register
     * @since 1
     */
    void registerGlobalPlaceholderFactory(
            @NotNull String identifier,
            @NotNull GlobalPlaceholderFactory placeholderFactory);

    /**
     * Registers a new individual placeholder. Any previously registered element (global or individual) with the
     * same identifier is overwritten. See {@link IndividualPlaceholder} to know more about individual placeholders.
     * <p>
     * This is a simplified method to register a placeholder by providing separately its replace function for
     * {@link IndividualPlaceholder#getReplacement(Player, String)} and a fixed refresh interval for
     * {@link Placeholder#getRefreshIntervalTicks()}
     *
     * @param identifier the case-insensitive identifier of the placeholder
     * @param refreshIntervalTicks the minimum interval in ticks between invocations of the replace function for
     *         each player (when the placeholder is in use), see {@link Placeholder#getRefreshIntervalTicks()}
     * @param replaceFunction the callback function to provide the replacement text to display
     * @since 1
     */
    void registerIndividualPlaceholder(
            @NotNull String identifier,
            int refreshIntervalTicks,
            @NotNull IndividualPlaceholderReplaceFunction replaceFunction);

    /**
     * Registers a new individual placeholder. Any previously registered element (global or individual) with the same
     * identifier is overwritten. See {@link IndividualPlaceholder} to know more about individual placeholders.
     *
     * @param identifier the case-insensitive identifier of the placeholder
     * @param placeholder the placeholder to register
     * @since 1
     */
    void registerIndividualPlaceholder(@NotNull String identifier, @NotNull IndividualPlaceholder placeholder);

    /**
     * Registers a new individual placeholder factory. Any previously registered element (global or individual) with the
     * same identifier is overwritten. See {@link IndividualPlaceholder} to know more about individual placeholders.
     * <p>
     * This method is more complex and not usually needed: it is necessary if parsing the argument of the placeholder is
     * performance intensive, for example a mathematical expression or a JSON string.
     * <p>
     * See {@link IndividualPlaceholderFactory} to know more about individual placeholder factories.
     *
     * @param identifier the case-insensitive identifier of the placeholder factory
     * @param placeholderFactory the placeholder factory to register
     * @since 1
     */
    void registerIndividualPlaceholderFactory(
            @NotNull String identifier,
            @NotNull IndividualPlaceholderFactory placeholderFactory);

    /**
     * Returns if a placeholder with a given identifier is registered.
     *
     * @param identifier the case-insensitive identifier of the placeholder
     * @since 1
     */
    boolean isRegisteredPlaceholder(@NotNull String identifier);

    /**
     * Returns all the registered placeholder identifiers.
     *
     * @return an immutable copy of the registered placeholder identifiers
     * @since 1
     */
    @NotNull Collection<String> getRegisteredPlaceholders();

    /**
     * Unregisters the placeholder with a given identifier.
     *
     * @param identifier the identifier of the placeholder to unregister, case-insensitive
     * @since 1
     */
    void unregisterPlaceholder(@NotNull String identifier);

    /**
     * Unregisters all placeholders.
     *
     * @since 1
     */
    void unregisterPlaceholders();

}
