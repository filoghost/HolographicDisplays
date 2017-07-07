package com.gmail.filoghost.holographicdisplays.object;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.object.PluginHologram;
import com.gmail.filoghost.holographicdisplays.object.PluginHologramManager;
import com.gmail.filoghost.holographicdisplays.placeholder.Placeholder;
import com.gmail.filoghost.holographicdisplays.placeholder.PlaceholdersRegister;
import com.gmail.filoghost.holographicdisplays.util.Validator;

public class BackendAPI {
	
	public static Hologram createHologram(Plugin plugin, Location source) {
		Validator.notNull(plugin, "plugin");
		Validator.notNull(source, "source");
		Validator.notNull(source.getWorld(), "source's world");
		Validator.isTrue(Bukkit.isPrimaryThread(), "Async hologram creation");
		
		PluginHologram hologram = new PluginHologram(source, plugin);
		PluginHologramManager.addHologram(hologram);
		
		return hologram;
	}
	
	public static boolean registerPlaceholder(Plugin plugin, String textPlaceholder, double refreshRate, PlaceholderReplacer replacer) {
		Validator.notNull(textPlaceholder, "textPlaceholder");
		Validator.isTrue(refreshRate >= 0, "refreshRate should be positive");
		Validator.notNull(replacer, "replacer");
		
		return PlaceholdersRegister.register(new Placeholder(plugin, textPlaceholder, refreshRate, replacer));
	}

	public static boolean isHologramEntity(Entity bukkitEntity) {
		Validator.notNull(bukkitEntity, "bukkitEntity");
		return HolographicDisplays.getNMSManager().isNMSEntityBase(bukkitEntity);
	}

	public static Collection<Hologram> getHolograms(Plugin plugin) {
		Validator.notNull(plugin, "plugin");
		return PluginHologramManager.getHolograms(plugin);
	}

	public static Collection<String> getRegisteredPlaceholders(Plugin plugin) {
		Validator.notNull(plugin, "plugin");
		return PlaceholdersRegister.getTextPlaceholdersByPlugin(plugin);
	}

	public static boolean unregisterPlaceholder(Plugin plugin, String textPlaceholder) {
		Validator.notNull(plugin, "plugin");
		Validator.notNull(textPlaceholder, "textPlaceholder");
		return PlaceholdersRegister.unregister(plugin, textPlaceholder);
	}

	public static void unregisterPlaceholders(Plugin plugin) {
		Validator.notNull(plugin, "plugin");
		for (String placeholder : getRegisteredPlaceholders(plugin)) {
			unregisterPlaceholder(plugin, placeholder);
		}
	}

}
