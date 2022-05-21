/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import me.filoghost.fcommons.logging.Log;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface ClickCallbackProvider {

    boolean hasClickCallback();

    void invokeClickCallback(Player player);

    default void logClickCallbackException(Plugin plugin, Player player, Throwable t) {
        Log.warning("The plugin " + plugin.getName() + " generated an exception"
                + " when the player " + player.getName() + " clicked a hologram.", t);
    }

}
