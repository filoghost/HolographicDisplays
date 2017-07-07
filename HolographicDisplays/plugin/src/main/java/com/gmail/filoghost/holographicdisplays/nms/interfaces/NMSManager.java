package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSHorse;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSSlime;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSWitherSkull;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;

public interface NMSManager {
	
	// A method to register all the custom entities of the plugin, it may fail.
	public void setup() throws Exception;
	
	public NMSArmorStand spawnNMSArmorStand(org.bukkit.World world, double x, double y, double z, CraftHologramLine parentPiece);

	public NMSHorse spawnNMSHorse(org.bukkit.World world, double x, double y, double z, CraftHologramLine parentPiece);
	
	public NMSWitherSkull spawnNMSWitherSkull(org.bukkit.World bukkitWorld, double x, double y, double z, CraftHologramLine parentPiece);
	
	public NMSItem spawnNMSItem(org.bukkit.World bukkitWorld, double x, double y, double z, CraftItemLine parentPiece, ItemStack stack);
	
	public NMSSlime spawnNMSSlime(org.bukkit.World bukkitWorld, double x, double y, double z, CraftTouchSlimeLine parentPiece);
	
	public boolean isNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);

	public NMSEntityBase getNMSEntityBase(org.bukkit.entity.Entity bukkitEntity);
	
	public FancyMessage newFancyMessage(String text);
	
	public boolean isUnloadUnsure(org.bukkit.Chunk bukkitChunk);

}
