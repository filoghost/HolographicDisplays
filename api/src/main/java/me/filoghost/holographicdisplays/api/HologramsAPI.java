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
package me.filoghost.holographicdisplays.api;

import me.filoghost.holographicdisplays.api.internal.BackendAPI;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * This the main class of the <b>Holographic Displays API</b>.
 * It provides methods to create holograms and to register custom placeholders.
 */
public class HologramsAPI {
    
    
    private HologramsAPI() {
        // No constructor needed.
    }
    
    
    /**
     * Creates a hologram at given location.
     * 
     * @param plugin the plugin that creates it
     * @param source the location where it will appear
     * @return the new hologram created
     */
    public static Hologram createHologram(Plugin plugin, Location source) {
        return BackendAPI.getImplementation().createHologram(plugin, source);
    }
    
    
    /**
     * Finds all the holograms created by a given plugin.
     * 
     * @param plugin the plugin to search for in holograms
     * @return the holograms created by a plugin. the Collection is a copy
     * and modifying it has no effect on the holograms.
     */
    public static Collection<Hologram> getHolograms(Plugin plugin) {
        return BackendAPI.getImplementation().getHolograms(plugin);
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
        return BackendAPI.getImplementation().registerPlaceholder(plugin, textPlaceholder, refreshRate, replacer);
    }
    
    
    /**
     * Finds all the placeholders registered by a given plugin.
     * 
     * @param plugin the plugin to search for
     * @return a collection of placeholders registered by the plugin
     */
    public static Collection<String> getRegisteredPlaceholders(Plugin plugin) {
        return BackendAPI.getImplementation().getRegisteredPlaceholders(plugin);
    }
    
    
    /**
     * Unregister a placeholder created by a plugin.
     * 
     * @param plugin the plugin that owns the placeholder
     * @param textPlaceholder the placeholder to remove
     * @return true if found and removed, false otherwise
     */
    public static boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
        return BackendAPI.getImplementation().unregisterPlaceholder(plugin, textPlaceholder);
    }
    
    
    /**
     * Resets and removes all the placeholders registered by a plugin. This is useful
     * when you have configurable placeholders and you want to remove all of them.
     * 
     * @param plugin the plugin that owns the placeholders
     */
    public static void unregisterPlaceholders(Plugin plugin) {
        BackendAPI.getImplementation().unregisterPlaceholders(plugin);
    }
    
    
    /**
     * Checks if an entity is part of a hologram.
     * 
     * @param bukkitEntity the entity to check
     * @return true if the entity is a part of a hologram
     */
    public static boolean isHologramEntity(Entity bukkitEntity) {
        return BackendAPI.getImplementation().isHologramEntity(bukkitEntity);
    }

}
