/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands;

import me.filoghost.holographicdisplays.Colors;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messages {
    
    public static void sendTitle(CommandSender recipient, String title) {
        recipient.sendMessage("" + Colors.PRIMARY_SHADOW + ChatColor.BOLD + "----- " 
                + title + Colors.PRIMARY_SHADOW + ChatColor.BOLD + " -----");
    }
    
    public static void sendTip(CommandSender recipient, String tip) {
        recipient.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "TIP:" + Colors.SECONDARY_SHADOW + " " + tip);
    }
    
    public static void sendWarning(CommandSender recipient, String warning) {
        recipient.sendMessage(ChatColor.RED + "( " + ChatColor.DARK_RED + ChatColor.BOLD + "!" + ChatColor.RED + " ) " 
                + Colors.SECONDARY_SHADOW + warning);
    }
    
}
