package com.gmail.filoghost.holographicdisplays.api;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.object.BackendAPI;

/**
 * This the main class of the <b>Holographic Displays API</b>.
 * It provides methods to create holograms and to register custom placeholders.
 */
public class HologramsAPI {
	
	
	/**
	 * Creates a hologram at given location.
	 * 
	 * @param plugin the plugin that creates it
	 * @param source the location where it will appear
	 * @return the new hologram created
	 */
	public static Hologram createHologram(Plugin plugin, Location source) {
		return BackendAPI.createHologram(plugin, source);
	}
	
	
	/**
	 * Finds all the holograms created by a given plugin.
	 * 
	 * @param plugin the plugin to search for in holograms
	 * @return the holograms created by a plugin. the Collection is a copy
	 * and modifying it has no effect on the holograms.
	 */
	public static Collection<Hologram> getHolograms(Plugin plugin) {
		return BackendAPI.getHolograms(plugin);
	}
	
	
	/**
	 * Registers a new placeholder that can be used in holograms created with commands.
	 * With this method, you can basically expand the core of HolographicDisplays.
	 * 
	 * @param plugin the owner plugin of the placeholder
	 * @param textPlaceholder the text that the placeholder will be associated to (e.g.: "{onlinePlayers}")
	 * @param refreshRate the refresh rate of the placeholder, in seconds. Keep in mind that the minimum is 0.1 seconds, and that will be rounded to tenths of seconds
	 * @param replacer the implementation that will return the text to replace the placeholder, where the update() method is called every <b>refreshRate</b> seconds
	 * @return true if the registration was successfull, false if it was already registered
	 */
	public static boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
		return BackendAPI.registerPlaceholder(plugin, textPlaceholder, refreshRate, replacer);
	}
	
	
	/**
	 * Finds all the placeholders registered by a given plugin.
	 * 
	 * @param plugin the plugin to search for
	 * @return a collection of placeholders registered by the plugin
	 */
	public static Collection<String> getRegisteredPlaceholders(Plugin plugin) {
		return BackendAPI.getRegisteredPlaceholders(plugin);
	}
	
	
	/**
	 * Unregister a placeholder created by a plugin.
	 * 
	 * @param plugin the plugin that owns the placeholder
	 * @param textPlaceholder the placeholder to remove
	 * @return true if found and removed, false otherwise
	 */
	public static boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
		return BackendAPI.unregisterPlaceholder(plugin, textPlaceholder);
	}
	
	
	/**
	 * Resets and removes all the placeholders registered by a plugin. This is useful
	 * when you have configurable placeholders and you want to remove all of them.
	 * 
	 * @param plugin the plugin that owns the placeholders
	 */
	public static void unregisterPlaceholders(Plugin plugin) {
		BackendAPI.unregisterPlaceholders(plugin);
	}
	
	
	/**
	 * Checks if an entity is part of a hologram.
	 * 
	 * @param bukkitEntity the entity to check
	 * @return true if the entity is a part of a hologram
	 */
	public static boolean isHologramEntity(Entity bukkitEntity) {
		return BackendAPI.isHologramEntity(bukkitEntity);
	}

}
