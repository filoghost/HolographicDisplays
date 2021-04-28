/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.api.internal;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class HolographicDisplaysAPIProvider {
    
    private static HolographicDisplaysAPIProvider implementation;
    
    public static void setImplementation(HolographicDisplaysAPIProvider implementation) {
        HolographicDisplaysAPIProvider.implementation = implementation;
    }
    
    public static HolographicDisplaysAPIProvider getImplementation() {
        if (implementation == null) {
            throw new IllegalStateException("No API implementation set. Is Holographic Displays enabled?");
        }
        
        return implementation;
    }
    
    public abstract HolographicDisplaysAPI getHolographicDisplaysAPI(Plugin plugin);
    
    public abstract boolean isHologramEntity(Entity entity);

}
