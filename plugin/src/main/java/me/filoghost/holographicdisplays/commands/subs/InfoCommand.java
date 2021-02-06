/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.command.CommandSender;

public class InfoCommand extends LineEditingCommand {

    public InfoCommand() {
        super("info", "details");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Shows the lines of a hologram.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        
        sender.sendMessage("");
        Messages.sendTitle(sender, "Lines of the hologram '" + hologram.getName() + "'");
        int index = 0;
        
        for (CraftHologramLine line : hologram.getLinesUnsafe()) {
            sender.sendMessage(Colors.SECONDARY + Colors.BOLD + (++index) + Colors.SECONDARY_SHADOW + ". " + Colors.SECONDARY + HologramDatabase.serializeHologramLine(line));
        }
        EditCommand.sendQuickEditCommands(context, hologram);
    }

}
