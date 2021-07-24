/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.hologram;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface StandardItemLine extends StandardTouchableLine {

    ItemStack getItemStack();

    void onPickup(Player player);

}
