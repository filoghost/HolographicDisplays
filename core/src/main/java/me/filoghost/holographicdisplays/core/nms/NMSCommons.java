/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.core.nms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class NMSCommons {

    // This is used on hologram icons, to prevent vanilla items from merging with them.
    public static final String ANTISTACK_LORE = ChatColor.BLACK.toString() + Math.random();

    private static final boolean IS_PAPER_SERVER = Bukkit.getName().equals("Paper");

    /**
     * Paper contains some code changes compared to Spigot.
     */
    public static boolean isPaperServer() {
        return IS_PAPER_SERVER;
    }

}
