/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.example.deathholograms;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DeathHolograms extends JavaPlugin implements Listener {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
    
    private HolographicDisplaysAPI holographicDisplaysAPI;

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }
        
        holographicDisplaysAPI = HolographicDisplaysAPI.get(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Hologram hologram = holographicDisplaysAPI.createHologram(event.getEntity().getEyeLocation());
        
        hologram.appendTextLine(ChatColor.RED + "Player " + ChatColor.GOLD + event.getEntity().getName() + ChatColor.RED + " died here!");
        hologram.appendTextLine(ChatColor.GRAY + "Time of death: " + TIME_FORMATTER.format(Instant.now()));
    }
    
}
