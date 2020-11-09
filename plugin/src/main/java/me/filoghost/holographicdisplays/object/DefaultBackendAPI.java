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
package me.filoghost.holographicdisplays.object;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.internal.BackendAPI;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.placeholder.Placeholder;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersRegister;
import me.filoghost.holographicdisplays.util.Validator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class DefaultBackendAPI extends BackendAPI {
	
	public Hologram createHologram(Plugin plugin, Location source) {
		Validator.notNull(plugin, "plugin");
		Validator.notNull(source, "source");
		Validator.notNull(source.getWorld(), "source's world");
		Validator.isTrue(Bukkit.isPrimaryThread(), "Async hologram creation");
		
		PluginHologram hologram = new PluginHologram(source, plugin);
		PluginHologramManager.addHologram(hologram);
		
		return hologram;
	}
	
	public boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
		Validator.notNull(textPlaceholder, "textPlaceholder");
		Validator.isTrue(refreshRate >= 0, "refreshRate should be positive");
		Validator.notNull(replacer, "replacer");
		
		return PlaceholdersRegister.register(new Placeholder(plugin, textPlaceholder, refreshRate, replacer));
	}

	public boolean isHologramEntity(Entity bukkitEntity) {
		Validator.notNull(bukkitEntity, "bukkitEntity");
		return HolographicDisplays.getNMSManager().isNMSEntityBase(bukkitEntity);
	}

	public Collection<Hologram> getHolograms(Plugin plugin) {
		Validator.notNull(plugin, "plugin");
		return PluginHologramManager.getHolograms(plugin);
	}

	public Collection<String> getRegisteredPlaceholders(Plugin plugin) {
		Validator.notNull(plugin, "plugin");
		return PlaceholdersRegister.getTextPlaceholdersByPlugin(plugin);
	}

	public boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
		Validator.notNull(plugin, "plugin");
		Validator.notNull(textPlaceholder, "textPlaceholder");
		return PlaceholdersRegister.unregister(plugin, textPlaceholder);
	}

	public void unregisterPlaceholders(Plugin plugin) {
		Validator.notNull(plugin, "plugin");
		for (String placeholder : getRegisteredPlaceholders(plugin)) {
			unregisterPlaceholder(plugin, placeholder);
		}
	}

}
