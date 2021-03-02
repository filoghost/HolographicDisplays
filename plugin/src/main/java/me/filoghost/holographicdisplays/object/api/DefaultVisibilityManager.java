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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultVisibilityManager implements VisibilityManager {

    private static final int VISIBILITY_DISTANCE_SQUARED = 64 * 64;
    
    private final StandardHologram hologram;
    private Map<String, Boolean> playersVisibilityMap;
    private boolean visibleByDefault;
    
    public DefaultVisibilityManager(StandardHologram hologram) {
        Preconditions.notNull(hologram, "hologram");
        this.hologram = hologram;
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
            if (playersVisibilityMap != null && playersVisibilityMap.containsKey(player.getName().toLowerCase())) {
                // Has a specific value set
                continue;
            }
                
            if (visibleByDefault) {
                // Now it's visible, and it previously wasn't because the value has changed
                sendCreatePacketIfNear(player, hologram);
            } else {
                // Opposite case: now it's not visible
                sendDestroyPacketIfNear(player, hologram);
            }
        }
    }
    
    @Override
    public void showTo(Player player) {
        Preconditions.notNull(player, "player");
        
        boolean wasVisible = isVisibleTo(player);
        
        if (playersVisibilityMap == null) {
            // Lazy initialization
            playersVisibilityMap = new ConcurrentHashMap<>();
        }
        
        playersVisibilityMap.put(player.getName().toLowerCase(), true);
        
        if (!wasVisible) {
            sendCreatePacketIfNear(player, hologram);
        }
    }
    
    
    @Override
    public void hideTo(Player player) {
        Preconditions.notNull(player, "player");
        
        boolean wasVisible = isVisibleTo(player);
        
        if (playersVisibilityMap == null) {
            // Lazy initialization
            playersVisibilityMap = new ConcurrentHashMap<>();
        }
        
        playersVisibilityMap.put(player.getName().toLowerCase(), false);
        
        if (wasVisible) {
            sendDestroyPacketIfNear(player, hologram);
        }
    }
    
    @Override
    public boolean isVisibleTo(Player player) {
        Preconditions.notNull(player, "player");
        
        if (playersVisibilityMap != null) {
            Boolean value = playersVisibilityMap.get(player.getName().toLowerCase());
            if (value != null) {
                return value;
            }
        }

        return visibleByDefault;
    }

    @Override
    public void resetVisibility(Player player) {
        Preconditions.notNull(player, "player");
        
        if (playersVisibilityMap == null) {
            return;
        }
        
        boolean wasVisible = isVisibleTo(player);
        
        playersVisibilityMap.remove(player.getName().toLowerCase());
        
        if (visibleByDefault && !wasVisible) {
            sendCreatePacketIfNear(player, hologram);
            
        } else if (!visibleByDefault && wasVisible) {
            sendDestroyPacketIfNear(player, hologram);
        }
    }
    
    @Override
    public void resetVisibilityAll() {
        if (playersVisibilityMap != null) {
            // We need to refresh all the players
            Set<String> playerNames = new HashSet<>(playersVisibilityMap.keySet());
            
            for (String playerName : playerNames) {
                Player onlinePlayer = Bukkit.getPlayerExact(playerName);
                if (onlinePlayer != null) {
                    resetVisibility(onlinePlayer);
                }
            }
            
            playersVisibilityMap.clear();
            playersVisibilityMap = null;
        }
    }
    
    private void sendCreatePacketIfNear(Player player, StandardHologram hologram) {
        if (ProtocolLibHook.isEnabled() && isNear(player, hologram)) {
            ProtocolLibHook.sendCreateEntitiesPacket(player, hologram);
        }
    }
    
    private void sendDestroyPacketIfNear(Player player, StandardHologram hologram) {
        if (ProtocolLibHook.isEnabled() && isNear(player, hologram)) {
            ProtocolLibHook.sendDestroyEntitiesPacket(player, hologram);
        }
    }
    
    private boolean isNear(Player player, StandardHologram hologram) {
        Location playerLocation = player.getLocation();
        Location hologramLocation = hologram.getLocation();
        
        return player.isOnline() 
                && playerLocation.getWorld().equals(hologramLocation.getWorld()) 
                && playerLocation.distanceSquared(hologramLocation) < VISIBILITY_DISTANCE_SQUARED;
    }

    @Override
    public String toString() {
        return "VisibilityManager [playersMap=" + playersVisibilityMap + ", visibleByDefault=" + visibleByDefault + "]";
    }
    
}
