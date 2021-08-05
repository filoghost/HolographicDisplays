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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MovehereCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public MovehereCommand(InternalHologramEditor hologramEditor) {
        super("movehere");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Moves a hologram to your location.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        InternalHologram hologram = hologramEditor.getHologram(args[0]);

        hologram.setPosition(player.getLocation());
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LOCATION);

        hologramEditor.teleportLookingDown(player, player.getLocation());
        player.sendMessage(ColorScheme.PRIMARY + "You moved the hologram '" + hologram.getName() + "' near to you.");
    }

}
