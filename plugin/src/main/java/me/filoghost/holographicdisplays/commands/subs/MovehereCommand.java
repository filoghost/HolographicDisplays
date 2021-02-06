/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.object.NamedHologram;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MovehereCommand extends HologramSubCommand {


    public MovehereCommand() {
        super("movehere");
        setMinArgs(1);
        setUsageArgs("<hologram>");
        setDescription("Moves a hologram to your location.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        NamedHologram hologram = HologramCommandValidate.getNamedHologram(args[0]);
        
        hologram.teleport(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        hologram.despawnEntities();
        hologram.refreshAll();
        
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        Location to = player.getLocation();
        to.setPitch(90);
        player.teleport(to, TeleportCause.PLUGIN);
        player.sendMessage(Colors.PRIMARY + "You moved the hologram '" + hologram.getName() + "' near to you.");
    }
    
}
