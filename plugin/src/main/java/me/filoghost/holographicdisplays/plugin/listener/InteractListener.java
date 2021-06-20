/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.listener;

import me.filoghost.holographicdisplays.core.nms.NMSManager;
import me.filoghost.holographicdisplays.core.nms.entity.NMSEntity;
import me.filoghost.holographicdisplays.core.hologram.StandardHologramLine;
import me.filoghost.holographicdisplays.core.hologram.StandardTouchableLine;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractListener implements Listener {
    
    private final NMSManager nmsManager;
    
    public InteractListener(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSlimeInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.SLIME) {
            return;
        }
            
        Player clicker = event.getPlayer();
        if (clicker.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        
        NMSEntity entityBase = nmsManager.getNMSEntityBase(event.getRightClicked());
        if (entityBase == null) {
            return;
        }

        StandardHologramLine line = entityBase.getHologramLine();
        if (!(line instanceof StandardTouchableLine)) {
            return;
        }

        ((StandardTouchableLine) line).onTouch(clicker);
    }
    
}
