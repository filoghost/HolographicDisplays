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
package me.filoghost.holographicdisplays.api.internal;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public abstract class BackendAPI {
	
	private static BackendAPI implementation;
	
	public static void setImplementation(BackendAPI implementation) {
		BackendAPI.implementation = implementation;
	}
	
	public static BackendAPI getImplementation() {
		if (implementation == null) {
			throw new IllegalStateException("No API implementation set. Is Holographic Displays enabled?");
		}
		
		return implementation;
	}

	public abstract Hologram createHologram(Plugin plugin, Location source);

	public abstract Collection<Hologram> getHolograms(Plugin plugin);

	public abstract boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer);

	public abstract Collection<String> getRegisteredPlaceholders(Plugin plugin);

	public abstract boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder);

	public abstract void unregisterPlaceholders(Plugin plugin);

	public abstract boolean isHologramEntity(Entity bukkitEntity);	
	

}
