/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package me.filoghost.holographicdisplays.plugin.format;

import me.filoghost.fcommons.Colors;
import me.filoghost.holographicdisplays.plugin.disk.StaticReplacements;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DisplayFormat {

    public static String apply(String input) {
        if (input == null) {
            return null;
        }

        input = StaticReplacements.searchAndReplace(input);
        input = input.replace("&u", "{rainbow}");
        input = Colors.colorize(input);
        return input;
    }

    public static void sendTitle(CommandSender recipient, String title) {
        recipient.sendMessage("");
        recipient.sendMessage(ColorScheme.PRIMARY_DARKER + ChatColor.BOLD + "----- "
                + title + ColorScheme.PRIMARY_DARKER + ChatColor.BOLD + " -----");
    }

    public static void sendTip(CommandSender recipient, String tip) {
        recipient.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "TIP:" + ColorScheme.SECONDARY_DARKER + " " + tip);
    }

    public static void sendWarning(CommandSender recipient, String warning) {
        recipient.sendMessage(ChatColor.RED + "( " + ChatColor.DARK_RED + ChatColor.BOLD + "!" + ChatColor.RED + " ) "
                + ColorScheme.SECONDARY_DARKER + warning);
    }

}
