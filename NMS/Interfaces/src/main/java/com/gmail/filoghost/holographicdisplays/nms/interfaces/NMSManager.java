package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import com.gmail.filoghost.holographicdisplays.api.line.HologramLine;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.*;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.message.FancyMessage;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSManager {

	/**
	 * A method to register all the custom entities of the plugin, it may fail.
	 */
	void setup() throws Exception;

	NMSArmorStand spawnNMSArmorStand(World world, double x, double y, double z, HologramLine parentPiece);

	NMSItem spawnNMSItem(World bukkitWorld, double x, double y, double z, ItemLine parentPiece, ItemStack stack, ItemPickupManager itemPickupManager);

	NMSSlime spawnNMSSlime(World bukkitWorld, double x, double y, double z, HologramLine parentPiece);

	boolean isNMSEntityBase(Entity bukkitEntity);

	NMSEntityBase getNMSEntityBase(Entity bukkitEntity);

	void sendFancyMessage(FancyMessage message, Player player);

	default void handleSpawnFail(HologramLine parentPiece) {
		ConsoleLogger.warning("Coulnd't spawn entity for this hologram: " + parentPiece.getParent());
	}
}
