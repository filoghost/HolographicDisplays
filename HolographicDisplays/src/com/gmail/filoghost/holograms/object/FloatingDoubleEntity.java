package com.gmail.filoghost.holograms.object;

import org.bukkit.World;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;

// Represents an object associated with a hologram, made of a wither skull with a passenger.
public abstract class FloatingDoubleEntity {

	public abstract void spawn(CraftHologram parent, World bukkitWorld, double x, double y, double z) throws SpawnFailedException;
	
	public abstract void despawn();

}
