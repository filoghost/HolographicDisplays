/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

import me.filoghost.holographicdisplays.api.line.ItemLine;
import org.bukkit.entity.Player;

public interface ItemPickupManager {

    void handleItemLinePickup(Player player, ItemLine itemLine);
    
}
