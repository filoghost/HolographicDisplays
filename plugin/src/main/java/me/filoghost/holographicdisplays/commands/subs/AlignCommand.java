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
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.object.NamedHologram;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class AlignCommand extends HologramSubCommand {

    public AlignCommand() {
        super("align");
        setMinArgs(3);
        setUsageArgs("<X | Y | Z | XZ> <hologram> <referenceHologram>");
        setDescription("Aligns the first hologram to the second, in the specified axis.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[1]);
        NamedHologram referenceHologram = HologramCommandValidate.getNamedHologram(args[2]);
        
        CommandValidate.check(hologram != referenceHologram, "The hologram must not be the same!");

        Location loc = hologram.getLocation();

        String axis = args[0];
        if (axis.equalsIgnoreCase("x")) {
            loc.setX(referenceHologram.getX());
        } else if (axis.equalsIgnoreCase("y")) {
            loc.setY(referenceHologram.getY());
        } else if (axis.equalsIgnoreCase("z")) {
            loc.setZ(referenceHologram.getZ());
        } else if (axis.equalsIgnoreCase("xz")) {
            loc.setX(referenceHologram.getX());
            loc.setZ(referenceHologram.getZ());
        } else {
            throw new CommandException("You must specify either X, Y, Z or XZ, " + axis + " is not a valid axis.");
        }

        hologram.teleport(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        hologram.despawnEntities();
        hologram.refreshAll();
            
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        sender.sendMessage(Colors.PRIMARY + "Hologram \"" + hologram.getName() + "\" aligned to the hologram \"" + referenceHologram.getName() + "\" on the " + axis.toUpperCase() + " axis.");
    }
    
}
