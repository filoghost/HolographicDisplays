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
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Arrays;
import java.util.List;

public class MovehereCommand extends HologramSubCommand {


    public MovehereCommand() {
        super("movehere");
        setPermission(Strings.BASE_PERM + "movehere");
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
        Player player = CommandValidator.getPlayerSender(sender);
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        
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

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Moves a hologram to your location.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
