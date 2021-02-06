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
import me.filoghost.holographicdisplays.commands.HologramSubCommand;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NearCommand extends HologramSubCommand {

    public NearCommand() {
        super("near");
        setMinArgs(1);
        setUsageArgs("<radius>");
        setDescription("Get a list of near holograms.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        Player player = CommandValidate.getPlayerSender(sender);
        int radius = CommandValidate.parseInteger(args[0]);
        CommandValidate.check(radius > 0, "Radius must be at least 1.");
        
        World world = player.getWorld();
        int radiusSquared = radius * radius;
        List<NamedHologram> nearHolograms = new ArrayList<>();
        
        for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
            if (hologram.getLocation().getWorld().equals(world) && hologram.getLocation().distanceSquared(player.getLocation()) <= radiusSquared) {
                nearHolograms.add(hologram);
            }
        }
        
        CommandValidate.check(!nearHolograms.isEmpty(), "There are no holograms in the given radius.");
        
        Messages.sendTitle(player, "Near holograms");
        for (NamedHologram nearHologram : nearHolograms) {
            player.sendMessage(Colors.SECONDARY_SHADOW + "- " + Colors.SECONDARY + Colors.BOLD + nearHologram.getName() + " " + Colors.SECONDARY_SHADOW + "at x: " + (int) nearHologram.getX() + ", y: " + (int) nearHologram.getY() + ", z: " + (int) nearHologram.getZ() + " (lines: " + nearHologram.size() + ")");
        }
    }
    
}
