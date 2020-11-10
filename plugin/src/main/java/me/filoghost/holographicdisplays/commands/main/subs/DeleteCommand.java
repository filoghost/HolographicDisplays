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
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends HologramSubCommand {

    public DeleteCommand() {
        super("delete", "remove");
        setPermission(Strings.BASE_PERM + "delete");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }



    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        
        hologram.delete();
        NamedHologramManager.removeHologram(hologram);
        HologramDatabase.deleteHologram(hologram.getName());
        
        HologramDatabase.trySaveToDisk();
        sender.sendMessage(Colors.PRIMARY + "You deleted the hologram '" + hologram.getName() + "'.");
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Deletes a hologram. Cannot be undone.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
