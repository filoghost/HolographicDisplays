package com.gmail.filoghost.holographicdisplays.object;

import org.bukkit.Location;

public class NamedHologram extends CraftHologram {

	private final String name;
	
	public NamedHologram(Location source, String name) {
		super(source);
		this.name = name;
		setAllowPlaceholders(true);
	}

	public String getName() {
		return name;
	}
	
	@Override
	public void delete() {
		super.delete();
		NamedHologramManager.removeHologram(this);
	}

	@Override
	public String toString() {
		return "NamedHologram [name=" + name + ", super=" + super.toString() + "]";
	}	
	
}
