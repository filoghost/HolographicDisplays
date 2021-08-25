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
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        DisplayFormat.sendTitle(sender, "How to edit the hologram \"" + hologram.getName() + "\"");
        for (HologramSubCommand subCommand : commandManager.getSubCommands()) {
            if (subCommand instanceof LineEditingCommand) {
                DisplayFormat.sendCommandDescription(
                        sender,
                        subCommand.getFullUsageText(context).replace("<hologram>", hologram.getName()),
                        subCommand.getDescription(context));
            }
        }

        if (sender instanceof Player) {
            DisplayFormat.sendHoverCommandDescriptionTip((Player) sender);
        }
    }

}
