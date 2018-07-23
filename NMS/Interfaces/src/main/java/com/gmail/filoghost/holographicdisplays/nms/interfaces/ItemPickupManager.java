package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import org.bukkit.entity.Player;

public interface ItemPickupManager {

	void handleItemLinePickup(Player player, PickupHandler pickupHandler, Hologram hologram);
}
