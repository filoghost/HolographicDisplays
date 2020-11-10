/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.example.deathholograms;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

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
