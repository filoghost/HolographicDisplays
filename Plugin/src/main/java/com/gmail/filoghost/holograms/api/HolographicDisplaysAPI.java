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
package com.gmail.filoghost.holograms.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holograms.api.replacements.FakeFloatingItem;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.internal.BackendAPI;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.PluginHologram;
import com.gmail.filoghost.holographicdisplays.object.PluginHologramManager;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.Validator;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public class HolographicDisplaysAPI {
	
	private static Set<String> notifiedPlugins = new HashSet<String>();
	
	private static void notifyOldAPI(Plugin plugin) {
		if (notifiedPlugins.add(plugin.getName())) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Holographic Displays] The plugin \"" + plugin.getName() + "\" is still using the old API of Holographic Displays. "
				+ "Please notify the author and ask them to update it, the old API will be removed soon.");
		}
	}
	
	@Deprecated
	public static Hologram createHologram(Plugin plugin, Location source, String... lines) {
		notifyOldAPI(plugin);
		CraftHologram hologram = (CraftHologram) BackendAPI.getImplementation().createHologram(plugin, source);
		for (String line : lines) {
			hologram.appendTextLine(line);
		}
		return hologram;
	}

	@Deprecated
	public static FloatingItem createFloatingItem(Plugin plugin, Location source, ItemStack itemstack) {
		notifyOldAPI(plugin);
		Validator.notNull(itemstack, "itemstack");
		Validator.isTrue(itemstack.getType() != Material.AIR, "itemstack cannot be AIR");
		
		CraftHologram hologram = (CraftHologram) BackendAPI.getImplementation().createHologram(plugin, source);
		hologram.appendItemLine(itemstack);
		return new FakeFloatingItem(hologram, itemstack);
	}
	
	@Deprecated
	public static Hologram createIndividualHologram(Plugin plugin, Location source, Player whoCanSee, String... lines) {
		notifyOldAPI(plugin);
		List<Player> whoCanSeeList = new ArrayList<Player>();
		whoCanSeeList.add(whoCanSee);
		return createIndividualHologram(plugin, source, whoCanSeeList, lines);
	}
	
	@Deprecated
	public static Hologram createIndividualHologram(Plugin plugin, Location source, List<Player> whoCanSee, String... lines) {
		notifyOldAPI(plugin);
		Validator.notNull(plugin, "plugin");
		Validator.notNull(source, "source");
		Validator.notNull(source.getWorld(), "source's world");
		
		CraftHologram hologram = (CraftHologram) BackendAPI.getImplementation().createHologram(plugin, source);
		
		hologram.getVisibilityManager().setVisibleByDefault(false);
		if (whoCanSee != null) {
			for (Player player : whoCanSee) {
				hologram.getVisibilityManager().showTo(player);
			}
		}
		
		for (String line : lines) {
			hologram.appendTextLine(line);
		}
		
		return hologram;
	}
	
	@Deprecated
	public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, Player whoCanSee, ItemStack itemstack) {
		notifyOldAPI(plugin);
		List<Player> whoCanSeeList = new ArrayList<Player>();
		whoCanSeeList.add(whoCanSee);
		return createIndividualFloatingItem(plugin, source, whoCanSeeList, itemstack);
	}
	
	@Deprecated
	public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, List<Player> whoCanSee, ItemStack itemstack) {
		notifyOldAPI(plugin);
		Validator.notNull(plugin, "plugin cannot be null");
		Validator.notNull(source, "source cannot be null");
		Validator.notNull(source.getWorld(), "source's world cannot be null");
		Validator.notNull(itemstack, "itemstack cannot be null");
		Validator.isTrue(itemstack.getType() != Material.AIR, "itemstack cannot be AIR");
		
		CraftHologram hologram = (CraftHologram) BackendAPI.getImplementation().createHologram(plugin, source);
		hologram.appendItemLine(itemstack);
		
		hologram.getVisibilityManager().setVisibleByDefault(false);
		if (whoCanSee != null) {
			for (Player player : whoCanSee) {
				hologram.getVisibilityManager().showTo(player);
			}
		}
		
		return new FakeFloatingItem(hologram, itemstack);
	}
	
	@Deprecated
	public static Hologram[] getHolograms(Plugin plugin) {
		notifyOldAPI(plugin);
		Validator.notNull(plugin, "plugin cannot be null");
		
		List<Hologram> pluginHolograms = Utils.newList();
		for (PluginHologram pluginHologram : PluginHologramManager.getHolograms()) {
			if (pluginHologram.getOwner().equals(plugin)) {
				pluginHolograms.add(pluginHologram);
			}
		}
		
		return pluginHolograms.toArray(new Hologram[0]);
	}
	
	@Deprecated
	public static FloatingItem[] getFloatingItems(Plugin plugin) {
		notifyOldAPI(plugin);
		Validator.notNull(plugin, "plugin cannot be null");
		return new FloatingItem[0];
	}
	
	@Deprecated
	public static boolean isHologramEntity(Entity bukkitEntity) {
		return HologramsAPI.isHologramEntity(bukkitEntity);
	}

}
