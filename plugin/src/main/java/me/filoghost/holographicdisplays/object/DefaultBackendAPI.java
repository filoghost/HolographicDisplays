/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.internal.BackendAPI;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.placeholder.Placeholder;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersRegister;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class DefaultBackendAPI extends BackendAPI {
    
    public Hologram createHologram(Plugin plugin, Location source) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(source, "source");
        Preconditions.notNull(source.getWorld(), "source's world");
        Preconditions.checkState(Bukkit.isPrimaryThread(), "Async hologram creation");
        
        PluginHologram hologram = new PluginHologram(source, plugin);
        PluginHologramManager.addHologram(hologram);
        
        return hologram;
    }
    
    public boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
        Preconditions.notNull(textPlaceholder, "textPlaceholder");
        Preconditions.checkArgument(refreshRate >= 0, "refreshRate should be positive");
        Preconditions.notNull(replacer, "replacer");
        
        return PlaceholdersRegister.register(new Placeholder(plugin, textPlaceholder, refreshRate, replacer));
    }

    public boolean isHologramEntity(Entity bukkitEntity) {
        Preconditions.notNull(bukkitEntity, "bukkitEntity");
        return HolographicDisplays.getNMSManager().isNMSEntityBase(bukkitEntity);
    }

    public Collection<Hologram> getHolograms(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        return PluginHologramManager.getHolograms(plugin);
    }

    public Collection<String> getRegisteredPlaceholders(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        return PlaceholdersRegister.getTextPlaceholdersByPlugin(plugin);
    }

    public boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
        Preconditions.notNull(plugin, "plugin");
        Preconditions.notNull(textPlaceholder, "textPlaceholder");
        return PlaceholdersRegister.unregister(plugin, textPlaceholder);
    }

    public void unregisterPlaceholders(Plugin plugin) {
        Preconditions.notNull(plugin, "plugin");
        for (String placeholder : getRegisteredPlaceholders(plugin)) {
            unregisterPlaceholder(plugin, placeholder);
        }
    }

}
