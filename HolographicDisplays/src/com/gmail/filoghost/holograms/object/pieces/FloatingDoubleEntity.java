package com.gmail.filoghost.holograms.object.pieces;

import org.bukkit.World;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.object.HologramBase;

// Represents an object associated with a hologram, made of a wither skull with a passenger.
public abstract class FloatingDoubleEntity {

	public abstract void spawn(HologramBase parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException;
	
	public abstract void despawn();
	
	public abstract void teleport(double x, double y, double z);

}
