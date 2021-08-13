/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.plugin.commands.HologramCommandManager;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
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
    private final InternalHologramEditor hologramEditor;

    public EditCommand(HologramCommandManager commandManager, InternalHologramEditor hologramEditor) {
        super("edit");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Lists the commands to edit a hologram.");

        this.commandManager = commandManager;
        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);

        sender.sendMessage("");
        DisplayFormat.sendTitle(sender, "How to edit the hologram \"" + hologram.getName() + "\"");
        for (HologramSubCommand subCommand : commandManager.getSubCommands()) {
            if (subCommand instanceof LineEditingCommand) {
                String usage = subCommand.getFullUsageText(context).replace("<hologram>", hologram.getName());

                if (sender instanceof Player) {
                    List<String> help = new ArrayList<>();
                    help.add(ColorScheme.PRIMARY + usage);
                    for (String tutLine : subCommand.getDescription(context)) {
                        help.add(ColorScheme.SECONDARY_DARKER + tutLine);
                    }

                    ((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
                            .color(ChatColor.AQUA)
                            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
                            .create());

                } else {
                    sender.sendMessage(ColorScheme.PRIMARY + usage);
                }
            }
        }

        if (sender instanceof Player) {
            HelpCommand.sendHoverTip((Player) sender);
        }
    }

}
