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
package com.gmail.filoghost.holographicdisplays.api.internal;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * @deprecated Please use the new API!
 */
@Deprecated
public abstract class BackendAPI {
	
	private static BackendAPI implementation;

	@Deprecated
	public static void setImplementation(BackendAPI implementation) {
		BackendAPI.implementation = implementation;
	}

	@Deprecated
	public static BackendAPI getImplementation() {
		if (implementation == null) {
			throw new IllegalStateException("No API implementation set. Is Holographic Displays enabled?");
		}
		
		return implementation;
	}

	@Deprecated
	public abstract Hologram createHologram(Plugin plugin, Location source);

	@Deprecated
	public abstract Collection<Hologram> getHolograms(Plugin plugin);

	@Deprecated
	public abstract boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer);

	@Deprecated
	public abstract Collection<String> getRegisteredPlaceholders(Plugin plugin);

	@Deprecated
	public abstract boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder);

	@Deprecated
	public abstract void unregisterPlaceholders(Plugin plugin);

	@Deprecated
	public abstract boolean isHologramEntity(Entity bukkitEntity);

}
