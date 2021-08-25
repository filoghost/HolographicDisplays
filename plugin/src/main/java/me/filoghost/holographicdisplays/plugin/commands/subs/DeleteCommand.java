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
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public DeleteCommand(InternalHologramEditor hologramEditor) {
        super("delete", "remove");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Deletes a hologram (cannot be undone!).");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);

        hologramEditor.delete(hologram);
        hologramEditor.saveChanges(hologram, ChangeType.DELETE);

        sender.sendMessage(ColorScheme.PRIMARY + "Hologram \"" + hologram.getName() + "\" deleted.");
    }

}
