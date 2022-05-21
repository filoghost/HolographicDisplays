/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.base;

import org.bukkit.entity.Player;

public interface PickupCallbackProvider {

    boolean hasPickupCallback();

    void invokePickupCallback(Player player);

}
