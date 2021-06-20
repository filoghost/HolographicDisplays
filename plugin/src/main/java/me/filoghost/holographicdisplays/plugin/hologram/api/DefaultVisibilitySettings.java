/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.api;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.VisibilitySettings;
import me.filoghost.holographicdisplays.plugin.bridge.protocollib.ProtocolLibHook;
import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.plugin.api.v2.V2VisibilityManagerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultVisibilitySettings implements VisibilitySettings {
    
    private final StandardHologram hologram;
    private final V2VisibilityManagerAdapter v2Adapter;
    private Map<UUID, Visibility> visibilityByPlayer;
    private Visibility defaultVisibility;
    
    public DefaultVisibilitySettings(StandardHologram hologram) {
        Preconditions.notNull(hologram, "hologram");
        this.hologram = hologram;
        this.v2Adapter = new V2VisibilityManagerAdapter(this);
        this.defaultVisibility = Visibility.VISIBLE;
    }
    
    @Override
    public @NotNull Visibility getDefaultVisibility() {
        return defaultVisibility;
    }
    
    @Override
    public void setDefaultVisibility(@NotNull Visibility defaultVisibility) {
        if (this.defaultVisibility == defaultVisibility) {
            return;
        }
        
        this.defaultVisibility = defaultVisibility;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (visibilityByPlayer != null && visibilityByPlayer.containsKey(player.getUniqueId())) {
                // Has a specific value set
                continue;
            }
            
            sendVisibilityChangePacket(hologram, player, defaultVisibility);
        }
    }
    
    @Override
    public void setIndividualVisibility(@NotNull Player player, @NotNull Visibility visibility) {
        Visibility oldVisibility = getVisibility(player);

        // Lazy initialization
        if (visibilityByPlayer == null) {
            visibilityByPlayer = new ConcurrentHashMap<>();
        }
        visibilityByPlayer.put(player.getUniqueId(), visibility);

        if (oldVisibility != visibility) {
            sendVisibilityChangePacket(hologram, player, visibility);
        }
    }
    
    @Override
    public boolean isVisibleTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        return getVisibility(player) == Visibility.VISIBLE;
    }
    
    private Visibility getVisibility(Player player) {
        if (visibilityByPlayer != null) {
            Visibility visibility = visibilityByPlayer.get(player.getUniqueId());
            if (visibility != null) {
                return visibility;
            }
        }

        return defaultVisibility;
    }

    @Override
    public void resetIndividualVisibility(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        if (visibilityByPlayer == null) {
            return;
        }
        
        Visibility oldVisibility = visibilityByPlayer.remove(player.getUniqueId());
        
        if (oldVisibility != null && oldVisibility != defaultVisibility) {
            sendVisibilityChangePacket(hologram, player, defaultVisibility);
        }
    }
    
    @Override
    public void resetIndividualVisibilityAll() {
        if (visibilityByPlayer == null) {
            return;
        }
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            Visibility oldVisibility = getVisibility(player);
            if (oldVisibility != defaultVisibility) {
                sendVisibilityChangePacket(hologram, player, defaultVisibility);
            }
        }
        visibilityByPlayer = null;
    }
    
    private void sendVisibilityChangePacket(StandardHologram hologram, Player player, Visibility visibility) {
        if (ProtocolLibHook.isEnabled()) {
            if (visibility == Visibility.VISIBLE) {
                ProtocolLibHook.sendCreateEntitiesPacket(player, hologram);
            } else {
                ProtocolLibHook.sendDestroyEntitiesPacket(player, hologram);
            }
        }
    }

    @Override
    public String toString() {
        return "VisibilitySettings [" 
                + "defaultVisibility=" + defaultVisibility 
                + ", visibilityByPlayer=" + visibilityByPlayer 
                + "]";
    }

    public V2VisibilityManagerAdapter getV2Adapter() {
        return v2Adapter;
    }

}
