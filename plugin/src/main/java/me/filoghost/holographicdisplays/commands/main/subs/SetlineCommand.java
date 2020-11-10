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
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class SetlineCommand extends HologramSubCommand {

    public SetlineCommand() {
        super("setline");
        setPermission(Strings.BASE_PERM + "setline");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> <lineNumber> <newText>";
    }

    @Override
    public int getMinimumArguments() {
        return 3;
    }


    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        String serializedLine = Utils.join(args, " ", 2, args.length);
        
        int lineNumber = CommandValidator.getInteger(args[1]);
        CommandValidator.isTrue(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
        int index = lineNumber - 1;
        
        CraftHologramLine line = CommandValidator.parseHologramLine(hologram, serializedLine, true);
        
        hologram.getLinesUnsafe().get(index).despawn();
        hologram.getLinesUnsafe().set(index, line);
        hologram.refreshAll();

        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " changed!");
        EditCommand.sendQuickEditCommands(sender, label, hologram.getName());
        
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Changes a line of a hologram.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.EDIT_LINES;
    }

}
