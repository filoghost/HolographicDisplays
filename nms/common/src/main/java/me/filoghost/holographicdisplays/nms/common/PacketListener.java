/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.common;

import org.bukkit.entity.Player;

public interface PacketListener {

    /**
     * Called asynchronously (from network threads) when a player interacts with an entity.
     *
     * @param entityID the ID of the entity
     * @return true if the packet should be cancelled, false otherwise
     */
    boolean onAsyncEntityInteract(Player player, int entityID);

}
