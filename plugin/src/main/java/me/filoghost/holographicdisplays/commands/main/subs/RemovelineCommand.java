/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class RemovelineCommand extends HologramSubCommand {

    public RemovelineCommand() {
        super("removeline");
        setPermission(Strings.BASE_PERM + "removeline");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> <lineNumber>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }


    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        
        int lineNumber = CommandValidator.getInteger(args[1]);

        CommandValidator.isTrue(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
        int index = lineNumber - 1;
        
        CommandValidator.isTrue(hologram.size() > 1, "The hologram should have at least 1 line. If you want to delete it, use /" + label + " delete.");

        hologram.removeLine(index);
        hologram.refreshAll();
        
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " removed!");
        EditCommand.sendQuickEditCommands(sender, label, hologram.getName());
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Removes a line from a hologram.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.EDIT_LINES;
    }

}
