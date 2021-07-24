/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.holographicdisplays.plugin.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import org.bukkit.command.CommandSender;

public class DebugCommand extends HologramSubCommand {

    public DebugCommand() {
        super("debug");
        setShowInHelpCommand(false);
        setDescription("Displays information useful for debugging.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) {
        sender.sendMessage(ColorScheme.ERROR + "This command is currently unused.");
    }

}
