package com.gmail.filoghost.holograms.object;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import com.gmail.filoghost.holograms.utils.Validator;
import com.gmail.filoghost.holograms.utils.VisibilityManager;

public abstract class HologramBase {
	
	protected String name;
	protected World bukkitWorld;
	protected double x;
	protected double y;
	protected double z;
	
	protected int chunkX;
	protected int chunkZ;
	
	protected VisibilityManager visibilityManager;
	
	private boolean deleted;
	
	protected HologramBase(String name, Location source) {
		this.name = name;
		bukkitWorld = source.getWorld();
		x = source.getX();
		y = source.getY();
		z = source.getZ();
		chunkX = source.getChunk().getX();
		chunkZ = source.getChunk().getZ();
	}

	public final String getName() {
		return name;
	}
	
	public final World getWorld() {
		return bukkitWorld;
	}
	
	public final Location getLocation() {
		return new Location(bukkitWorld, x, y, z);
	}
	
	public final int getBlockX() {
		return (int) x;
	}
	
	public final int getBlockY() {
		return (int) y;
	}
	
	public final int getBlockZ() {
		return (int) z;
	}
	
	public final double getX() {
		return x;
	}
	
	public final double getY() {
		return y;
	}
	
	public final double getZ() {
		return z;
	}

	public final int getChunkX() {
		return chunkX;
	}
	
	public final int getChunkZ() {
		return chunkZ;
	}
	
	public final boolean isInChunk(Chunk chunk) {
		return chunkX == chunk.getX() && chunkZ == chunk.getZ();
	}
	
	public final boolean isInLoadedChunk() {
		return bukkitWorld.isChunkLoaded(chunkX, chunkZ);
	}
	
	public void setVisibilityManager(VisibilityManager visibilityManager) {
		this.visibilityManager = visibilityManager;
	}
	
	public boolean hasVisibilityManager() {
		return visibilityManager != null;
	}
	
	public VisibilityManager getVisibilityManager() {
		return visibilityManager;
	}
	
	public final void delete() {
		deleted = true;
		onDeleteEvent();		
	}
	
	public final boolean isDeleted() {
		return deleted;
	}
	
	public final void setLocation(Location source) {
		Validator.notNull(source, "location cannot be null");
		Validator.notNull(source.getWorld(), "location's world cannot be null");
		
		bukkitWorld = source.getWorld();
		x = source.getX();
		y = source.getY();
		z = source.getZ();
		chunkX = source.getChunk().getX();
		chunkZ = source.getChunk().getZ();
	}

	public abstract void onDeleteEvent();
	
	public abstract boolean update();
	
	public abstract void hide();
	
}
