/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.common.nms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class NMSCommons {

    /**
     * Lore is used on hologram icons, to prevent vanilla items from merging with them.
     */
    public static final String ANTI_STACK_LORE = ChatColor.BLACK.toString() + Math.random();

    /**
     * Paper contains some code changes compared to Spigot.
     */
    public static final boolean IS_PAPER_SERVER = Bukkit.getName().equals("Paper");

}
