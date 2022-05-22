/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.api.beta.Position;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.command.CommandSender;

public class AlignCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public AlignCommand(InternalHologramEditor hologramEditor) {
        super("align");
        setMinArgs(3);
        setUsageArgs("<X | Y | Z | XZ> <hologramToAlign> <referenceHologram>");
        setDescription("Aligns a hologram to another along the specified axis.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[1]);
        InternalHologram referenceHologram = hologramEditor.getExistingHologram(args[2]);

        CommandValidate.check(hologram != referenceHologram, "The holograms must not be the same.");

        Position referencePosition = referenceHologram.getPosition();
        Position oldPosition = hologram.getPosition();
        Position newPosition;

        String axis = args[0];
        if (axis.equalsIgnoreCase("x")) {
            newPosition = Position.of(oldPosition.getWorldName(), referencePosition.getX(), oldPosition.getY(), oldPosition.getZ());
        } else if (axis.equalsIgnoreCase("y")) {
            newPosition = Position.of(oldPosition.getWorldName(), oldPosition.getX(), referencePosition.getY(), oldPosition.getZ());
        } else if (axis.equalsIgnoreCase("z")) {
            newPosition = Position.of(oldPosition.getWorldName(), oldPosition.getX(), oldPosition.getY(), referencePosition.getZ());
        } else if (axis.equalsIgnoreCase("xz")) {
            newPosition = Position.of(oldPosition.getWorldName(), referencePosition.getX(), oldPosition.getY(), referencePosition.getZ());
        } else {
            throw new CommandException("You must specify either X, Y, Z or XZ, " + axis + " is not a valid axis.");
        }

        hologram.setPosition(newPosition);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_POSITION);

        sender.sendMessage(ColorScheme.PRIMARY + "Hologram \"" + hologram.getName() + "\""
                + " aligned to the hologram \"" + referenceHologram.getName() + "\""
                + " on the " + axis.toUpperCase() + " axis.");
    }

}
