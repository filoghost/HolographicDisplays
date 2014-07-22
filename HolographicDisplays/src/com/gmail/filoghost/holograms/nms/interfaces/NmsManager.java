package com.gmail.filoghost.holograms.nms.interfaces;

import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.exception.SpawnFailedException;
import com.gmail.filoghost.holograms.object.HologramBase;

public interface NmsManager {
	
	public void registerCustomEntities() throws Exception;

	public HologramHorse spawnHologramHorse(org.bukkit.World world, double x, double y, double z) throws SpawnFailedException;
	
	public HologramWitherSkull spawnHologramWitherSkull(org.bukkit.World bukkitWorld, double x, double y, double z) throws SpawnFailedException;
	
	public CustomItem spawnCustomItem(org.bukkit.World bukkitWorld, double x, double y, double z,  ItemStack stack) throws SpawnFailedException;
	
	public TouchSlime spawnTouchSlime(org.bukkit.World bukkitWorld, double x, double y, double z) throws SpawnFailedException;
	
	public boolean isHologramComponent(org.bukkit.entity.Entity bukkitEntity);
	
	public boolean isBasicEntityNMS(org.bukkit.entity.Entity bukkitEntity);
	
	// Return null if not a hologram's (or floating item's) part.
	public HologramBase getParentHologram(org.bukkit.entity.Entity bukkitEntity);
	
	public FancyMessage newFancyMessage(String text);

	public boolean hasChatHoverFeature();

}
