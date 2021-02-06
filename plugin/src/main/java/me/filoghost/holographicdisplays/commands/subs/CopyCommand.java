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
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.command.CommandSender;

public class CopyCommand extends HologramSubCommand {
    
    public CopyCommand() {
        super("copy");
        setMinArgs(2);
        setUsageArgs("<fromHologram> <toHologram>");
        setDescription("Copies the contents of a hologram into another one.");
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram fromHologram = HologramCommandValidate.getNamedHologram(args[0]);
        NamedHologram toHologram = HologramCommandValidate.getNamedHologram(args[1]);
        
        toHologram.clearLines();
        for (CraftHologramLine line : fromHologram.getLinesUnsafe()) {
            CraftHologramLine clonedLine = HologramCommandValidate.parseHologramLine(toHologram, HologramDatabase.serializeHologramLine(line), false);
            toHologram.getLinesUnsafe().add(clonedLine);
        }
        
        toHologram.refreshAll();
        
        HologramDatabase.saveHologram(toHologram);
        HologramDatabase.trySaveToDisk();
        
        sender.sendMessage(Colors.PRIMARY + "Hologram \"" + fromHologram.getName() + "\" copied into hologram \"" + toHologram.getName() + "\"!");
    }

}
