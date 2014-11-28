package com.gmail.filoghost.holograms.api;

import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

import java.util.List;

import net.minecraft.util.com.google.common.collect.Lists;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holograms.nms.interfaces.NmsManager;
import com.gmail.filoghost.holograms.object.APICraftHologram;
import com.gmail.filoghost.holograms.object.APIFloatingItemManager;
import com.gmail.filoghost.holograms.object.APIHologramManager;
import com.gmail.filoghost.holograms.object.CraftFloatingItem;
import com.gmail.filoghost.holograms.utils.Validator;
import com.gmail.filoghost.holograms.utils.VisibilityManager;

public class HolographicDisplaysAPI {
	
	/**
	 * Creates a hologram at given location.
	 * @param plugin - the plugin that creates it.
	 * @param source - the location where it will appear.
	 * @param lines - the lines of the new hologram.
	 * @return the new hologram created.
	 */
	public static Hologram createHologram(Plugin plugin, Location source, String... lines) {
		
		Validator.notNull(plugin, "plugin cannot be null");
		Validator.notNull(source, "source cannot be null");
		Validator.notNull(source.getWorld(), "source's world cannot be null");
		
		APICraftHologram hologram = new APICraftHologram(source);
		APIHologramManager.addHologram(plugin, hologram);
		
		if (lines != null && lines.length > 0) {
			for (String line : lines) {
				hologram.addLine(line);
			}
		}
		
		hologram.update();
		return hologram;
	}
	
	/**
	 * Creates a floating item at given location.
	 * @param plugin - the plugin that creates it.
	 * @param source - the location where it will appear.
	 * @param itemstack - the floating item that will appear.
	 * @return the new floating item created.
	 */
	public static FloatingItem createFloatingItem(Plugin plugin, Location source, ItemStack itemstack) {
		
		Validator.notNull(plugin, "plugin cannot be null");
		Validator.notNull(source, "source cannot be null");
		Validator.notNull(source.getWorld(), "source's world cannot be null");
		Validator.notNull(itemstack, "itemstack cannot be null");
		Validator.checkArgument(itemstack.getType() != Material.AIR, "itemstack cannot be AIR");
		
		CraftFloatingItem floatingItem = new CraftFloatingItem(source, itemstack);
		APIFloatingItemManager.addFloatingItem(plugin, floatingItem);
		
		floatingItem.update();
		return floatingItem;
	}
	
	/**
	 * Creates a hologram at given location that only a player can see. If the provided player is null, no one will be able to see it.
	 * IMPORTANT NOTE: Requires ProtocolLib.
	 * @param plugin - the plugin that creates it.
	 * @param source - the location where it will appear.
	 * @param whoCanSee - the player who can see it.
	 * @param lines - the lines of the new hologram.
	 * @return the new hologram created.
	 */
	public static Hologram createIndividualHologram(Plugin plugin, Location source, Player whoCanSee, String... lines) {
		return createIndividualHologram(plugin, source, Lists.newArrayList(whoCanSee), lines);
	}
	
	/**
	 * Creates a hologram at given location that only a list of players can see. If the provided list is null, no one will be able to see it.
	 * IMPORTANT NOTE: Requires ProtocolLib.
	 * @param plugin - the plugin that creates it.
	 * @param source - the location where it will appear.
	 * @param whoCanSee - a list of players who can see it.
	 * @param lines - the lines of the new hologram.
	 * @return the new hologram created.
	 */
	public static Hologram createIndividualHologram(Plugin plugin, Location source, List<Player> whoCanSee, String... lines) {
		
		Validator.notNull(plugin, "plugin cannot be null");
		Validator.notNull(source, "source cannot be null");
		Validator.notNull(source.getWorld(), "source's world cannot be null");
		
		APICraftHologram hologram = new APICraftHologram(source);

		VisibilityManager visibilityManager = new VisibilityManager();
		hologram.setVisibilityManager(visibilityManager);
		
		if (whoCanSee != null) {
			for (Player player : whoCanSee) {
				hologram.getVisibilityManager().showTo(player);
			}
		}
		
		APIHologramManager.addHologram(plugin, hologram);
		
		if (lines != null && lines.length > 0) {
			for (String line : lines) {
				hologram.addLine(line);
			}
		}
		
		hologram.update();
		return hologram;
	}
	
	/**
	 * Creates a floating item at given location that only a player can see. If the provided player is null, no one will be able to see it.
	 * IMPORTANT NOTE: Requires ProtocolLib.
	 * @param plugin - the plugin that creates it.
	 * @param source - the location where it will appear.
	 * @param whoCanSee - the player who can see it.
	 * @param itemstack - the floating item that will appear.
	 * @return the new hologram created.
	 */
	public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, Player whoCanSee, ItemStack itemstack) {
		return createIndividualFloatingItem(plugin, source, Lists.newArrayList(whoCanSee), itemstack);
	}
	
	/**
	 * Creates a floating item at given location that only a list of players can see. If the provided list is null, no one will be able to see it.
	 * IMPORTANT NOTE: Requires ProtocolLib.
	 * @param plugin - the plugin that creates it.
	 * @param source - the location where it will appear.
	 * @param whoCanSee - a list of players who can see it.
	 * @param itemstack - the floating item that will appear.
	 * @return the new hologram created.
	 */
	public static FloatingItem createIndividualFloatingItem(Plugin plugin, Location source, List<Player> whoCanSee, ItemStack itemstack) {
		
		Validator.notNull(plugin, "plugin cannot be null");
		Validator.notNull(source, "source cannot be null");
		Validator.notNull(source.getWorld(), "source's world cannot be null");
		Validator.notNull(itemstack, "itemstack cannot be null");
		Validator.checkArgument(itemstack.getType() != Material.AIR, "itemstack cannot be AIR");
		
		CraftFloatingItem floatingItem = new CraftFloatingItem(source, itemstack);

		VisibilityManager visibilityManager = new VisibilityManager();
		floatingItem.setVisibilityManager(visibilityManager);
		
		if (whoCanSee != null) {
			for (Player player : whoCanSee) {
				floatingItem.getVisibilityManager().showTo(player);
			}
		}
		
		APIFloatingItemManager.addFloatingItem(plugin, floatingItem);
		
		floatingItem.update();
		return floatingItem;
	}
	
	/**
	 * @return a copy of all the holograms created with the API by a plugin.
	 */
	public static Hologram[] getHolograms(Plugin plugin) {
		Validator.notNull(plugin, "plugin cannot be null");
		return APIHologramManager.getHolograms(plugin);
	}
	
	/**
	 * @return a copy of all the holograms created with the API by a plugin.
	 */
	public static FloatingItem[] getFloatingItems(Plugin plugin) {
		Validator.notNull(plugin, "plugin cannot be null");
		return APIFloatingItemManager.getFloatingItems(plugin);
	}
	
	/**
	 * @return if the entity is part of a hologram.
	 */
	public static boolean isHologramEntity(Entity bukkitEntity) {
		Validator.notNull(bukkitEntity, "entity cannot be null");
		return nmsManager.isHologramComponent(bukkitEntity);
	}
	
	/**
	 * @deprecated for advanced use only. May change in the future.
	 */
	@Deprecated
	public static NmsManager getNmsManager() {
		return nmsManager;
	}
}
