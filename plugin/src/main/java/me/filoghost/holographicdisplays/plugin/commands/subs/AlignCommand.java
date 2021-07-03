/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class AlignCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public AlignCommand(InternalHologramEditor hologramEditor) {
        super("align");
        setMinArgs(3);
        setUsageArgs("<X | Y | Z | XZ> <hologram> <referenceHologram>");
        setDescription("Aligns the first hologram to the second, in the specified axis.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getHologram(args[1]);
        InternalHologram referenceHologram = hologramEditor.getHologram(args[2]);

        CommandValidate.check(hologram != referenceHologram, "The holograms must not be the same.");

        Location newLocation = hologram.getLocation();

        String axis = args[0];
        if (axis.equalsIgnoreCase("x")) {
            newLocation.setX(referenceHologram.getX());
        } else if (axis.equalsIgnoreCase("y")) {
            newLocation.setY(referenceHologram.getY());
        } else if (axis.equalsIgnoreCase("z")) {
            newLocation.setZ(referenceHologram.getZ());
        } else if (axis.equalsIgnoreCase("xz")) {
            newLocation.setX(referenceHologram.getX());
            newLocation.setZ(referenceHologram.getZ());
        } else {
            throw new CommandException("You must specify either X, Y, Z or XZ, " + axis + " is not a valid axis.");
        }

        hologram.teleport(newLocation);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LOCATION);

        sender.sendMessage(ColorScheme.PRIMARY + "Hologram \"" + hologram.getName() + "\""
                + " aligned to the hologram \"" + referenceHologram.getName() + "\""
                + " on the " + axis.toUpperCase() + " axis.");
    }

}
