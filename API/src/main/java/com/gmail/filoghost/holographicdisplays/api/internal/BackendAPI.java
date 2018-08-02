package com.gmail.filoghost.holographicdisplays.api.internal;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
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
