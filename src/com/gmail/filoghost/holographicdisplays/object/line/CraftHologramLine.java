package com.gmail.filoghost.holographicdisplays.object.line;

import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.util.Validator;

public abstract class CraftHologramLine implements HologramLine {
	
	private final double height;
	private final CraftHologram parent;
	
	// This field is necessary for teleport.
	private boolean isSpawned;
	
	protected CraftHologramLine(double height, CraftHologram parent) {
		Validator.notNull(parent, "parent hologram");
		this.height = height;
		this.parent = parent;
	}
	
	public final double getHeight() {
		return height;
	}

	@Override
	public final CraftHologram getParent() {
		return parent;
	}
	
	public void removeLine() {
		parent.removeLine(this);
	}

	public void spawn(World world, double x, double y, double z) {
		Validator.notNull(world, "world");
		
		// Remove the old entities when spawning the new ones.
		despawn();
		isSpawned = true;
		
		// Do nothing, there are no entities in this class.
	}
	
	public void despawn() {
		isSpawned = false;
	}
	
	public final boolean isSpawned() {
		return isSpawned;
	}
	
	public abstract int[] getEntitiesIDs();
	
	public abstract void teleport(double x, double y, double z);

}
