/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CopyCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public CopyCommand(InternalHologramEditor hologramEditor) {
        super("copy");
        setMinArgs(2);
        setUsageArgs("<fromHologram> <toHologram>");
        setDescription("Copies the contents of a hologram into another one.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram fromHologram = hologramEditor.getHologram(args[0]);
        InternalHologram toHologram = hologramEditor.getHologram(args[1]);

        List<InternalHologramLine> clonedLines = new ArrayList<>();
        for (InternalHologramLine line : fromHologram.getLines()) {
            clonedLines.add(hologramEditor.parseHologramLine(toHologram, line.getSerializedConfigValue()));
        }

        toHologram.getLines().setAll(clonedLines);
        hologramEditor.saveChanges(toHologram, ChangeType.EDIT_LINES);

        sender.sendMessage(ColorScheme.PRIMARY + "Hologram \"" + fromHologram.getName() + "\""
                + " copied into hologram \"" + toHologram.getName() + "\".");
    }

}
