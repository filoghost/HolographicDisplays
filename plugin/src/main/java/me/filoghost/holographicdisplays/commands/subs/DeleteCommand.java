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
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends HologramSubCommand {

    public DeleteCommand() {
        super("delete", "remove");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Deletes a hologram. Cannot be undone.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        
        hologram.delete();
        NamedHologramManager.removeHologram(hologram);
        HologramDatabase.deleteHologram(hologram.getName());
        
        HologramDatabase.trySaveToDisk();
        sender.sendMessage(Colors.PRIMARY + "You deleted the hologram '" + hologram.getName() + "'.");
    }

}
