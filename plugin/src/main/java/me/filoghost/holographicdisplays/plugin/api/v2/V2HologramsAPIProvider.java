/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.api.v2;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.internal.HologramsAPIProvider;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.collection.CollectionUtils;
import me.filoghost.holographicdisplays.api.placeholder.RegisteredPlaceholder;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class V2HologramsAPIProvider extends HologramsAPIProvider {

    private final V2HologramManager hologramManager;
    private final PlaceholderRegistry placeholderRegistry;

    public V2HologramsAPIProvider(V2HologramManager hologramManager, PlaceholderRegistry placeholderRegistry) {
        this.hologramManager = hologramManager;
        this.placeholderRegistry = placeholderRegistry;
    }

    @Override
    public Hologram createHologram(Plugin plugin, Location source) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(source, "source");
        Preconditions.notNull(source.getWorld(), "source.getWorld()");
        Preconditions.checkMainThread("async hologram creation");

        return hologramManager.createHologram(new ImmutablePosition(source), plugin);
    }

    @Override
    public Collection<Hologram> getHolograms(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        List<Hologram> ownedHolograms = new ArrayList<>();

        for (V2Hologram hologram : hologramManager.getHolograms()) {
            if (hologram.getCreatorPlugin().equals(plugin)) {
                ownedHolograms.add(hologram);
            }
        }

        return Collections.unmodifiableList(ownedHolograms);
    }

    @Override
    public boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(textPlaceholder, "textPlaceholder");
        Preconditions.checkArgument(refreshRate >= 0, "refreshRate should be positive");
        Preconditions.notNull(replacer, "replacer");

        int refreshIntervalTicks = Math.min((int) (refreshRate * 20.0), 1);
        boolean alreadyRegistered = placeholderRegistry.isRegisteredIdentifier(plugin, textPlaceholder);

        if (!alreadyRegistered) {
            placeholderRegistry.registerGlobalPlaceholderReplacer(
                    plugin,
                    textPlaceholder,
                    refreshIntervalTicks,
                    argument -> replacer.update());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<String> getRegisteredPlaceholders(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");

        List<RegisteredPlaceholder> registeredPlaceholders = placeholderRegistry.getRegisteredPlaceholders(plugin);
        return CollectionUtils.toArrayList(registeredPlaceholders, RegisteredPlaceholder::getIdentifier);
    }

    @Override
    public boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(textPlaceholder, "textPlaceholder");

        boolean registered = placeholderRegistry.isRegisteredIdentifier(plugin, textPlaceholder);

        if (registered) {
            placeholderRegistry.unregister(plugin, textPlaceholder);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unregisterPlaceholders(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");

        placeholderRegistry.unregisterAll(plugin);
    }

    @Override
    public boolean isHologramEntity(Entity bukkitEntity) {
        Preconditions.notNull(bukkitEntity, "bukkitEntity");
        return false;
    }

}
