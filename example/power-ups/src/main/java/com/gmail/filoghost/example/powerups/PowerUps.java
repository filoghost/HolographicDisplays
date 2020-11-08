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
package com.gmail.filoghost.example.powerups;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;

public class PowerUps extends JavaPlugin implements Listener {
	
	@Override
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
	public void onEntityDeath(EntityDeathEvent event) {
		
		if (event.getEntityType() == EntityType.ZOMBIE) {

			// Remove normal drops and exp.
			event.getDrops().clear();
			event.setDroppedExp(0);
			
			// Spawn the floating item with a label.
			final Hologram hologram = HologramsAPI.createHologram(this, event.getEntity().getLocation().add(0.0, 0.9, 0.0));
			hologram.appendTextLine(ChatColor.AQUA  + "" + ChatColor.BOLD + "Speed PowerUp");
			ItemLine icon = hologram.appendItemLine(new ItemStack(Material.SUGAR));
			
			icon.setPickupHandler(new PickupHandler() {
				
				@Override
				public void onPickup(Player player) {
					
					// Play an effect.
					player.playEffect(hologram.getLocation(), Effect.MOBSPAWNER_FLAMES, null);
					
					// 30 seconds of speed II.
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30 * 20, 1), true);
					
					// Delete the hologram.
					hologram.delete();
					
				}
			});
			
		}
	}
}
