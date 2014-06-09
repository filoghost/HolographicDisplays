package com.gmail.filoghost.holograms.api;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holograms.nms.interfaces.NmsManager;
import com.gmail.filoghost.holograms.object.APIHologramManager;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.utils.GenericUtils;
import com.gmail.filoghost.holograms.utils.Validator;
import com.gmail.filoghost.holograms.utils.VisibilityManager;
import static com.gmail.filoghost.holograms.HolographicDisplays.nmsManager;

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
		
		CraftHologram hologram = new CraftHologram("{API-Hologram}", source);
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
	 * Creates a hologram at given location that only a player can see. If the provided player is null, no one will be able to see it.
	 * IMPORTANT NOTE: Requires ProtocolLib.
	 * @param plugin - the plugin that creates it.
	 * @param source - the location where it will appear.
	 * @param whoCanSee - the player who can see it.
	 * @param lines - the lines of the new hologram.
	 * @return the new hologram created.
	 */
	public static Hologram createIndividualHologram(Plugin plugin, Location source, Player whoCanSee, String... lines) {
		return createIndividualHologram(plugin, source, GenericUtils.createList(whoCanSee), lines);
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
		
		CraftHologram hologram = new CraftHologram("{API-Hologram}", source);

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
	 * @return a copy of all the holograms created with the API by a plugin.
	 */
	public static Hologram[] getHolograms(Plugin plugin) {
		Validator.notNull(plugin, "plugin cannot be null");
		return APIHologramManager.getHolograms(plugin);
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
