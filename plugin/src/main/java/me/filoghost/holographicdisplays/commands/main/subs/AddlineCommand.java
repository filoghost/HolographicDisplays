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
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class AddlineCommand extends HologramSubCommand {

    public AddlineCommand() {
        super("addline");
        setPermission(Strings.BASE_PERM + "addline");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> <text>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        String serializedLine = Utils.join(args, " ", 1, args.length);
        
        CraftHologramLine line = CommandValidator.parseHologramLine(hologram, serializedLine, true);
        hologram.getLinesUnsafe().add(line);
        hologram.refreshAll();
            
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line added!");
        EditCommand.sendQuickEditCommands(sender, label, hologram.getName());
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Adds a line to an existing hologram.");
    }

    @Override
    public SubCommandType getType() {
        return SubCommandType.EDIT_LINES;
    }

}
