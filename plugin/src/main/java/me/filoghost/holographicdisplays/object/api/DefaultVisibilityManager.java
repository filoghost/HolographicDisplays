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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
                
            if (visibleByDefault) {
                // Now it's visible, and it previously wasn't because the value has changed
                sendCreatePacket(player, hologram);
            } else {
                // Opposite case: now it's not visible
                sendDestroyPacket(player, hologram);
            }
        }
    }
    
    @Override
    public void showTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        boolean wasVisible = isVisibleTo(player);
        
        if (playersVisibilityMap == null) {
            // Lazy initialization
            playersVisibilityMap = new ConcurrentHashMap<>();
        }
        
        playersVisibilityMap.put(player.getUniqueId(), true);
        
        if (!wasVisible) {
            sendCreatePacket(player, hologram);
        }
    }
    
    
    @Override
    public void hideTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        boolean wasVisible = isVisibleTo(player);
        
        if (playersVisibilityMap == null) {
            // Lazy initialization
            playersVisibilityMap = new ConcurrentHashMap<>();
        }
        
        playersVisibilityMap.put(player.getUniqueId(), false);
        
        if (wasVisible) {
            sendDestroyPacket(player, hologram);
        }
    }
    
    @Override
    public boolean isVisibleTo(@NotNull Player player) {
        Preconditions.notNull(player, "player");
        
        if (playersVisibilityMap != null) {
            Boolean value = playersVisibilityMap.get(player.getUniqueId());
            if (value != null) {
                return value;
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
        
        boolean wasVisible = isVisibleTo(player);
        
        playersVisibilityMap.remove(player.getUniqueId());
        
        if (visibleByDefault && !wasVisible) {
            sendCreatePacket(player, hologram);
            
        } else if (!visibleByDefault && wasVisible) {
            sendDestroyPacket(player, hologram);
        }
    }
    
    @Override
    public void resetVisibilityAll() {
        if (playersVisibilityMap != null) {
            // We need to refresh all the players
            Set<UUID> playerIDs = new HashSet<>(playersVisibilityMap.keySet());
            
            for (UUID playerID : playerIDs) {
                Player onlinePlayer = Bukkit.getPlayer(playerID);
                if (onlinePlayer != null) {
                    resetVisibility(onlinePlayer);
                }
            }
            
            playersVisibilityMap.clear();
            playersVisibilityMap = null;
        }
    }
    
    private void sendCreatePacket(Player player, StandardHologram hologram) {
        if (ProtocolLibHook.isEnabled()) {
            ProtocolLibHook.sendCreateEntitiesPacket(player, hologram);
        }
    }
    
    private void sendDestroyPacket(Player player, StandardHologram hologram) {
        if (ProtocolLibHook.isEnabled()) {
            ProtocolLibHook.sendDestroyEntitiesPacket(player, hologram);
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
