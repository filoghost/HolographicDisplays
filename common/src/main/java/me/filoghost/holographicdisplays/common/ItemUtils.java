/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common;

import org.bukkit.ChatColor;

public class ItemUtils {
    
    // This is used on hologram icons, to prevent vanilla items from merging with them.
    public static final String ANTISTACK_LORE = ChatColor.BLACK.toString() + Math.random();

}
