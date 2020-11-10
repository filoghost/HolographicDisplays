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
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Arrays;
import java.util.List;


public class TeleportCommand extends HologramSubCommand {
    
    public TeleportCommand() {
        super("teleport", "tp");
        setPermission(Strings.BASE_PERM + "teleport");
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
        
        Location loc = hologram.getLocation();
        loc.setPitch(90);
        player.teleport(loc, TeleportCause.PLUGIN);
        player.sendMessage(Colors.PRIMARY + "You were teleported to the hologram named '" + hologram.getName() + "'.");

    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Teleports you to the given hologram.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
