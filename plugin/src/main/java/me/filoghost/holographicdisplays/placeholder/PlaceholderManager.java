/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.placeholder.registry.PlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PlaceholderManager {

    private final PlaceholderRegistry placeholderRegistry;
    private final PlaceholdersUpdateTask placeholdersUpdateTask;

    public PlaceholderManager() {
        this.placeholderRegistry = new PlaceholderRegistry();
        PlaceholdersReplacementTracker placeholdersReplacementTracker = new PlaceholdersReplacementTracker(placeholderRegistry);
        this.placeholdersUpdateTask = new PlaceholdersUpdateTask(placeholdersReplacementTracker, placeholderRegistry);
        
        placeholderRegistry.setChangeListener(placeholdersReplacementTracker::clearOutdatedSources);
    }

    public PlaceholderRegistry getPlaceholderRegistry() {
        return placeholderRegistry;
    }

    public void startUpdaterTask(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, placeholdersUpdateTask, 0, 1);
    }

    public void updateTracking(StandardTextLine line) {
        placeholdersUpdateTask.updateTracking(line);
    }

}
