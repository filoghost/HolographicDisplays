/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.object.api;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.VisibilityManager;
import me.filoghost.holographicdisplays.bridge.protocollib.ProtocolLibHook;
import me.filoghost.holographicdisplays.core.hologram.StandardHologram;
import me.filoghost.holographicdisplays.legacy.api.v2.V2VisibilityManagerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultVisibilityManager implements VisibilityManager {
    
    private final StandardHologram hologram;
    private final V2VisibilityManagerAdapter v2Adapter;
    private Map<UUID, Boolean> playersVisibilityMap;
    private boolean visibleByDefault;
    
    public DefaultVisibilityManager(StandardHologram hologram) {
        Preconditions.notNull(hologram, "hologram");
        this.hologram = hologram;
        this.v2Adapter = new V2VisibilityManagerAdapter(this);
        this.visibleByDefault = true;
    }
    
    @Override
    public boolean isVisibleByDefault() {
        return visibleByDefault;
    }
    
    @Override
    public void setVisibleByDefault(boolean visibleByDefault) {
        if (this.visibleByDefault == visibleByDefault) {
            return;
        }
        
        this.visibleByDefault = visibleByDefault;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playersVisibilityMap != null && playersVisibilityMap.containsKey(player.getUniqueId())) {
                // Has a specific value set
                continue;
            }
            
            sendVisibilityChangePacket(hologram, player, visibleByDefault);
        }
    }
    
    @Override
    public void showTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");

        setVisibleTo(player, true);
    }
    
    
    @Override
    public void hideTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        setVisibleTo(player, false);
    }
    
    private void setVisibleTo(Player player, boolean visible) {
        boolean wasVisible = isVisibleTo(player);

        // Lazy initialization
        if (playersVisibilityMap == null) {
            playersVisibilityMap = new HashMap<>();
        }
        playersVisibilityMap.put(player.getUniqueId(), visible);

        if (wasVisible != visible) {
            sendVisibilityChangePacket(hologram, player, visible);
        }
    }
    
    @Override
    public boolean isVisibleTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        if (playersVisibilityMap != null) {
            Boolean visible = playersVisibilityMap.get(player.getUniqueId());
            if (visible != null) {
                return visible;
            }
        }

        return visibleByDefault;
    }

    @Override
    public void resetVisibility(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        if (playersVisibilityMap == null) {
            return;
        }
        
        Boolean wasVisible = playersVisibilityMap.remove(player.getUniqueId());        
        if (wasVisible != null && visibleByDefault != wasVisible) {
            sendVisibilityChangePacket(hologram, player, visibleByDefault);
        }
    }
    
    @Override
    public void resetVisibilityAll() {
        if (playersVisibilityMap == null) {
            return;
        }
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            boolean wasVisible = isVisibleTo(player);
            if (visibleByDefault != wasVisible) {
                sendVisibilityChangePacket(hologram, player, visibleByDefault);
            }
        }
        playersVisibilityMap = null;
    }
    
    private void sendVisibilityChangePacket(StandardHologram hologram, Player player, boolean visible) {
        if (ProtocolLibHook.isEnabled()) {
            if (visible) {
                ProtocolLibHook.sendCreateEntitiesPacket(player, hologram);
            } else {
                ProtocolLibHook.sendDestroyEntitiesPacket(player, hologram);
            }
        }
    }

    @Override
    public String toString() {
        return "VisibilityManager [playersMap=" + playersVisibilityMap + ", visibleByDefault=" + visibleByDefault + "]";
    }

    public V2VisibilityManagerAdapter getV2Adapter() {
        return v2Adapter;
    }

}
