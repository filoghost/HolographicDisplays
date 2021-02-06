/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.object.NamedHologram;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class RemovelineCommand extends LineEditingCommand {

    public RemovelineCommand() {
        super("removeline");
        setMinArgs(2);
        setUsageArgs("<hologram> <lineNumber>");
        setDescription("Removes a line from a hologram.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        
        int lineNumber = CommandValidate.parseInteger(args[1]);

        CommandValidate.check(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
        int index = lineNumber - 1;
        
        CommandValidate.check(hologram.size() > 1, "The hologram should have at least 1 line. If you want to delete it, use /" + context.getRootLabel() + " delete.");

        hologram.removeLine(index);
        hologram.refreshAll();
        
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " removed!");
        EditCommand.sendQuickEditCommands(context, hologram);
    }

}
