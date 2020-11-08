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
package com.gmail.filoghost.holographicdisplays.object;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.util.Validator;

/**
 * This class is only used by the plugin itself. Do not attempt to use it.
 */
public class PluginHologram extends CraftHologram {
	
	private Plugin plugin;

	public PluginHologram(Location source, Plugin plugin) {
		super(source);
		Validator.notNull(plugin, "plugin");
		this.plugin = plugin;
	}
	
	public Plugin getOwner() {
		return plugin;
	}
	
	@Override
	public void delete() {
		super.delete();
		PluginHologramManager.removeHologram(this);
	}
	
}
