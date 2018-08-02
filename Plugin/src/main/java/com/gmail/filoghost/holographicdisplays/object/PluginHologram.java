package com.gmail.filoghost.holographicdisplays.object;

import com.gmail.filoghost.holographicdisplays.util.Validator;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

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
