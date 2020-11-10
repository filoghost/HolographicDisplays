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
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CopyCommand extends HologramSubCommand {
    
    public CopyCommand() {
        super("copy");
        setPermission(Strings.BASE_PERM + "copy");
    }

    @Override
    public String getPossibleArguments() {
        return "<fromHologram> <toHologram>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram fromHologram = CommandValidator.getNamedHologram(args[0]);
        NamedHologram toHologram = CommandValidator.getNamedHologram(args[1]);
        
        toHologram.clearLines();
        for (CraftHologramLine line : fromHologram.getLinesUnsafe()) {
            CraftHologramLine clonedLine = CommandValidator.parseHologramLine(toHologram, HologramDatabase.serializeHologramLine(line), false);
            toHologram.getLinesUnsafe().add(clonedLine);
        }
        
        toHologram.refreshAll();
        
        HologramDatabase.saveHologram(toHologram);
        HologramDatabase.trySaveToDisk();
        
        sender.sendMessage(Colors.PRIMARY + "Hologram \"" + fromHologram.getName() + "\" copied into hologram \"" + toHologram.getName() + "\"!");
    }
    
    @Override
    public List<String> getTutorial() {
        return Arrays.asList(
                "Copies the contents of a hologram into another one.");
    }

    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
