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
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TeleportCommand extends HologramSubCommand {

    private final InternalHologramEditor hologramEditor;

    public TeleportCommand(InternalHologramEditor hologramEditor) {
        super("teleport", "tp");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Teleports to a hologram.");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        InternalHologram hologram = hologramEditor.getExistingHologram(args[0]);

        hologramEditor.teleportLookingDown(player, hologram.getBasePosition().toLocation());
        player.sendMessage(ColorScheme.PRIMARY + "Teleported to the hologram \"" + hologram.getName() + "\".");
    }

}
