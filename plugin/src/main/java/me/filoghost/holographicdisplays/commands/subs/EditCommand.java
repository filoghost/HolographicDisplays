/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import me.filoghost.holographicdisplays.object.internal.InternalHologramManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditCommand extends HologramSubCommand {
    
    private final HologramCommandManager commandManager;
    private final InternalHologramManager internalHologramManager;

    public EditCommand(HologramCommandManager commandManager, InternalHologramManager internalHologramManager) {
        super("edit");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Shows the commands to manipulate an existing hologram.");

        this.commandManager = commandManager;
        this.internalHologramManager = internalHologramManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = HologramCommandValidate.getInternalHologram(internalHologramManager, args[0]);
        
        sender.sendMessage("");
        Messages.sendTitle(sender, "How to edit the hologram '" + hologram.getName() + "'");
        for (HologramSubCommand subCommand : commandManager.getSubCommands()) {
            if (subCommand instanceof LineEditingCommand) {
                String usage = subCommand.getFullUsageText(context).replace("<hologram>", hologram.getName());

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
            HelpCommand.sendHoverTip((Player) sender);
        }
    }

}
