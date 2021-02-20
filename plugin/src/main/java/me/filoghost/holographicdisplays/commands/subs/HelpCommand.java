/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.commands.Messages;
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

public class HelpCommand extends HologramSubCommand {

    private final HologramCommandManager commandManager;

    public HelpCommand(HologramCommandManager commandManager) {
        super("help");
        setShowInHelpCommand(false);
        setDescription("Show the list of commands.");
        
        this.commandManager = commandManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        sender.sendMessage("");
        Messages.sendTitle(sender, "Holographic Displays Commands");
        for (HologramSubCommand subCommand : commandManager.getSubCommands()) {
            if (subCommand.isShowInHelpCommand()) {
                String usage = subCommand.getFullUsageText(context);

                if (sender instanceof Player) {
                    List<String> help = new ArrayList<>();
                    help.add(Colors.PRIMARY + usage);
                    for (String tutLine : subCommand.getDescription(context)) {
                        help.add(Colors.SECONDARY_SHADOW + tutLine);
                    }
                    
                    ((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
                        .color(ChatColor.AQUA)
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
                        .create());
                    
                } else {
                    sender.sendMessage(Colors.PRIMARY + usage);
                }
            }
        }

        if (sender instanceof Player) {
            sendHoverTip((Player) sender);
        }
    }

    public static void sendHoverTip(Player player) {
        player.sendMessage("");
        player.spigot().sendMessage(new ComponentBuilder("TIP:").color(ChatColor.YELLOW).bold(true)
            .append(" Try to ", FormatRetention.NONE).color(ChatColor.GRAY)
            .append("hover").color(ChatColor.WHITE).underlined(true)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Hover on the commands to get info about them.")))
            .append(" or ", FormatRetention.NONE).color(ChatColor.GRAY)
            .append("click").color(ChatColor.WHITE).underlined(true)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Click on the commands to insert them in the chat.")))
            .append(" on the commands.", FormatRetention.NONE).color(ChatColor.GRAY)
            .create());
    }

}
