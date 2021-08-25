/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.hologram.base;

import me.filoghost.fcommons.logging.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Collectable {

    boolean hasPickupCallback();

    void invokePickupCallback(Player player);

    default void logPickupCallbackException(Plugin plugin, Player player, Throwable t) {
        Log.warning("The plugin " + plugin.getName() + " generated an exception"
                + " when the player " + player.getName() + " picked up an item from a hologram.", t);
    }

}
