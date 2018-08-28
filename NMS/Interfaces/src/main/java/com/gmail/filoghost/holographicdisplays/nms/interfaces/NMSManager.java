package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;

public interface NMSManager {
	
	// A method to register all the custom entities of the plugin, it may fail.
	public void setup() throws Exception;
	
	public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, HologramLine parentPiece);
	
	public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack,  ItemPickupManager itemPickupManager);
	
	public NMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, HologramLine parentPiece);
	
	public boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

	public NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);
	
	public FancyMessage newFancyMessage(String text);

}
