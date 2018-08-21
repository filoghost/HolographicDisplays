package com.gmail.filoghost.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class DeathHolograms extends JavaPlugin implements Listener {
	
	public void onEnable() {
		
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
			getLogger().severe("*** This plugin will be disabled. ***");
			this.setEnabled(false);
			return;
		}
		
		Bukkit.getPluginManager().registerEvents(this, this);
		
	}
	
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		Hologram hologram = HologramsAPI.createHologram(this, event.getEntity().getEyeLocation());
		
		hologram.appendTextLine(ChatColor.RED + "Player " + ChatColor.GOLD + event.getEntity().getName() + ChatColor.RED + " died here!");
		hologram.appendTextLine(ChatColor.GRAY + "Time of death: " + new SimpleDateFormat("H:m").format(new Date()));
		
	}
}
