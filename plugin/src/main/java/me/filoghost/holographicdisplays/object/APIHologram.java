/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class APIHologram extends BaseHologram {
    
    private final Plugin plugin;
    private final APIHologramManager apiHologramManager;

    protected APIHologram(Location source, Plugin plugin, NMSManager nmsManager, APIHologramManager apiHologramManager) {
        super(source, nmsManager);
        Preconditions.notNull(plugin, "plugin");
        this.plugin = plugin;
        this.apiHologramManager = apiHologramManager;
    }
    
    public Plugin getOwner() {
        return plugin;
    }
    
    @Override
    public void delete() {
        apiHologramManager.deleteHologram(this);
    }
    
}
