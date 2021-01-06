/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.Permissions;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class InfoCommand extends HologramSubCommand {

    public InfoCommand() {
        super("info", "details");
        setPermission(Permissions.COMMAND_BASE + "info");
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
        
        sender.sendMessage("");
        Messages.sendTitle(sender, "Lines of the hologram '" + hologram.getName() + "'");
        int index = 0;
        
        for (CraftHologramLine line : hologram.getLinesUnsafe()) {
            sender.sendMessage(Colors.SECONDARY + Colors.BOLD + (++index) + Colors.SECONDARY_SHADOW + ". " + Colors.SECONDARY + HologramDatabase.serializeHologramLine(line));
        }
        EditCommand.sendQuickEditCommands(sender, label, hologram.getName());
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Shows the lines of a hologram.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.EDIT_LINES;
    }

}
