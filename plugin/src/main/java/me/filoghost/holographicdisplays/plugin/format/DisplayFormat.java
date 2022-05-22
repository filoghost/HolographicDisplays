/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package me.filoghost.holographicdisplays.plugin.format;

import me.filoghost.fcommons.Colors;
import me.filoghost.holographicdisplays.api.beta.Position;
import me.filoghost.holographicdisplays.plugin.config.StaticReplacements;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DisplayFormat {

    public static String apply(String input) {
        return apply(input, true);
    }

    public static String apply(String input, boolean addColors) {
        if (input == null) {
            return null;
        }

        input = StaticReplacements.searchAndReplace(input);
        input = input.replace("&u", "{rainbow}");
        if (addColors) {
            input = Colors.colorize(input);
        }
        return input;
    }

    public static void sendTitle(CommandSender recipient, String title) {
        recipient.sendMessage("");
        recipient.sendMessage("" + ColorScheme.PRIMARY_DARK + ChatColor.BOLD + "----- "
                + title + ColorScheme.PRIMARY_DARK + ChatColor.BOLD + " -----");
    }

    public static void sendTip(CommandSender recipient, String tip) {
        recipient.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "TIP:" + ColorScheme.SECONDARY_DARK + " " + tip);
    }

    public static void sendWarning(CommandSender recipient, String warning) {
        recipient.sendMessage(ChatColor.RED + "( " + ChatColor.DARK_RED + ChatColor.BOLD + "!" + ChatColor.RED + " ) "
                + ColorScheme.SECONDARY_DARK + warning);
    }

    public static void sendCommandDescription(CommandSender sender, String usage, List<String> description) {
        if (sender instanceof Player) {
            List<String> tooltipLines = new ArrayList<>();
            tooltipLines.add(ColorScheme.PRIMARY + usage);
            for (String descriptionLine : description) {
                tooltipLines.add(ColorScheme.SECONDARY_DARK + descriptionLine);
            }

            ((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
                    .color(ColorScheme.PRIMARY)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", tooltipLines))))
                    .create());

        } else {
            sender.sendMessage(ColorScheme.PRIMARY + usage);
        }
    }

    public static void sendHoverCommandDescriptionTip(Player player) {
        player.sendMessage("");
        player.spigot().sendMessage(new ComponentBuilder("TIP:").color(ChatColor.YELLOW).bold(true)
                .append(" Try to ", FormatRetention.NONE).color(ColorScheme.SECONDARY_DARK)
                .append("hover").color(ColorScheme.SECONDARY).underlined(true)
                .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Hover on the commands to see the description.")))
                .append(" or ", FormatRetention.NONE).color(ColorScheme.SECONDARY_DARK)
                .append("click").color(ColorScheme.SECONDARY).underlined(true)
                .event(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Click on the commands to insert them in the chat.")))
                .append(" on the commands.", FormatRetention.NONE).color(ColorScheme.SECONDARY_DARK)
                .create());
    }

    public static void sendHologramSummary(CommandSender sender, InternalHologram hologram, boolean showWorld) {
        Position position = hologram.getPosition();
        sender.sendMessage(ColorScheme.SECONDARY_DARK + "- " + ColorScheme.SECONDARY_BOLD + hologram.getName()
                + ColorScheme.SECONDARY_DARK + " (" + hologram.getLines().size() + " lines) at "
                + (showWorld ? "world: \"" + position.getWorldName() + "\", " : "")
                + "x: " + position.getBlockX() + ", "
                + "y: " + position.getBlockY() + ", "
                + "z: " + position.getBlockZ());
    }

}
