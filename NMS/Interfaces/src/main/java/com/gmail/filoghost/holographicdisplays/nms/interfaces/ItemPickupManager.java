package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;

public interface ItemPickupManager {

	public void handleItemLinePickup(Player player, PickupHandler pickupHandler, Hologram hologram);
	
}
