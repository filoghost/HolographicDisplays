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
package com.gmail.filoghost.holographicdisplays.api;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.api.internal.BackendAPI;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public class HologramsAPI {

    private HologramsAPI() {}

    @Deprecated
    public static Hologram createHologram(Plugin plugin, Location source) {
        return BackendAPI.getImplementation().createHologram(plugin, source);
    }

    @Deprecated
    public static Collection<Hologram> getHolograms(Plugin plugin) {
        return BackendAPI.getImplementation().getHolograms(plugin);
    }

    @Deprecated
    public static boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
        return BackendAPI.getImplementation().registerPlaceholder(plugin, textPlaceholder, refreshRate, replacer);
    }
    
    @Deprecated
    public static Collection<String> getRegisteredPlaceholders(Plugin plugin) {
        return BackendAPI.getImplementation().getRegisteredPlaceholders(plugin);
    }

    @Deprecated
    public static boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
        return BackendAPI.getImplementation().unregisterPlaceholder(plugin, textPlaceholder);
    }
    
    @Deprecated
    public static void unregisterPlaceholders(Plugin plugin) {
        BackendAPI.getImplementation().unregisterPlaceholders(plugin);
    }

    @Deprecated
    public static boolean isHologramEntity(Entity bukkitEntity) {
        return BackendAPI.getImplementation().isHologramEntity(bukkitEntity);
    }

}
