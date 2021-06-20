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
import me.filoghost.holographicdisplays.common.nms.NMSManager;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologram;
import me.filoghost.holographicdisplays.plugin.hologram.api.APIHologramManager;
import me.filoghost.holographicdisplays.plugin.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class V2HologramsAPIProvider extends HologramsAPIProvider {

    private final APIHologramManager apiHologramManager;
    private final NMSManager nmsManager;
    private final PlaceholderRegistry placeholderRegistry;

    public V2HologramsAPIProvider(APIHologramManager apiHologramManager, NMSManager nmsManager, PlaceholderRegistry placeholderRegistry) {
        this.apiHologramManager = apiHologramManager;
        this.nmsManager = nmsManager;
        this.placeholderRegistry = placeholderRegistry;
    }

    @Override
    public Hologram createHologram(Plugin plugin, Location source) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(source, "source");
        Preconditions.notNull(source.getWorld(), "source's world");
        Preconditions.checkState(Bukkit.isPrimaryThread(), "async hologram creation");

        return apiHologramManager.createHologram(source, plugin).getV2Adapter();
    }
    
    @Override
    public Collection<Hologram> getHolograms(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        List<Hologram> ownedHolograms = new ArrayList<>();

        for (APIHologram hologram : apiHologramManager.getHolograms()) {
            if (hologram.getCreatorPlugin().equals(plugin)) {
                ownedHolograms.add(hologram.getV2Adapter());
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
        
        return placeholderRegistry.getRegisteredIdentifiers(plugin);
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
        return nmsManager.isNMSEntityBase(bukkitEntity);
    }

}
