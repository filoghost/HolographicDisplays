/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface IndividualCustomName {

    String get(Player player);

}
