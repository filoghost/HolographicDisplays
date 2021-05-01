/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api;

import me.filoghost.holographicdisplays.api.internal.HolographicDisplaysAPIProvider;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Main entry point for accessing the Holographic Displays API.
 * 
 * @since 1
 */
public interface HolographicDisplaysAPI {

    /**
     * Returns the API version number, which is increased every time the API changes. This number is used to check if a
     * certain method or class (which may have been added later) is present or not.
     * <p>
     * All public API classes and methods are documented with the Javadoc tag {@code @since}, indicating in which API
     * version that element was introduced.
     * <p>
     * It can be used it to require a minimum version, as features may be added (rarely removed) in future versions. The
     * first version of the API is 1.
     * <p>
     * The API version is independent from the normal plugin version.
     *
     * @return the current API version
     * @since 1
     */
    static int getVersion() {
        return 1;
    }

    @NotNull
    static HolographicDisplaysAPI get(@NotNull Plugin plugin) {
        return HolographicDisplaysAPIProvider.getImplementation().getHolographicDisplaysAPI(plugin);
    }

    /**
     * Creates a hologram at given location.
     *
     * @param source the location where it will appear
     * @return the created hologram
     * @since 1
     */
    @NotNull
    Hologram createHologram(@NotNull Location source);

    /**
     * Returns all the active holograms. A hologram is no longer active after {@link Hologram#delete()} is invoked.
     *
     * @return an immutable collection of active holograms
     * @since 1
     */
    @NotNull
    Collection<Hologram> getHolograms();

    /**
     * @since 1
     */
    void registerPlaceholder(@NotNull String identifier, int refreshIntervalTicks, @NotNull PlaceholderReplacer replacer);

    /**
     * Returns all the registered placeholder identifiers.
     *
     * @return a collection of placeholder identifiers
     * @since 1
     */
    @NotNull
    Collection<String> getRegisteredPlaceholders();

    /**
     * Unregisters a placeholder.
     *
     * @param identifier the identifier of the placeholder to remove
     * @since 1
     */
    void unregisterPlaceholder(@NotNull String identifier);

    /**
     * Resets and removes all the registered placeholders.
     * <p>
     * May be useful to invoke before registering all the placeholders of your plugin.
     * 
     * @since 1
     */
    void unregisterPlaceholders();

    /**
     * Checks if an entity is part of a hologram.
     *
     * @param entity the entity to check
     * @return if the entity is part of a hologram
     * @since 1
     */
    static boolean isHologramEntity(@NotNull Entity entity) {
        return HolographicDisplaysAPIProvider.getImplementation().isHologramEntity(entity);
    }

}
