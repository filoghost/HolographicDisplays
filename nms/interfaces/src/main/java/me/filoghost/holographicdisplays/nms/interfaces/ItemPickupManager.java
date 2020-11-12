/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.interfaces;

import me.filoghost.holographicdisplays.api.Hologram;
import me.filoghost.holographicdisplays.api.handler.PickupHandler;
import org.bukkit.entity.Player;

public interface ItemPickupManager {

    void handleItemLinePickup(Player player, PickupHandler pickupHandler, Hologram hologram);
    
}
