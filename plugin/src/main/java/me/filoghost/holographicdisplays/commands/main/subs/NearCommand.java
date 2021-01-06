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
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearCommand extends HologramSubCommand {

    public NearCommand() {
        super("near");
        setPermission(Permissions.COMMAND_BASE + "near");
    }

    @Override
    public String getPossibleArguments() {
        return "<radius>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        Player player = CommandValidator.getPlayerSender(sender);
        int radius = CommandValidator.getInteger(args[0]);
        CommandValidator.isTrue(radius > 0, "Radius must be at least 1.");
        
        World world = player.getWorld();
        int radiusSquared = radius * radius;
        List<NamedHologram> nearHolograms = new ArrayList<>();
        
        for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
            if (hologram.getLocation().getWorld().equals(world) && hologram.getLocation().distanceSquared(player.getLocation()) <= radiusSquared) {
                nearHolograms.add(hologram);
            }
        }
        
        CommandValidator.isTrue(!nearHolograms.isEmpty(), "There are no holograms in the given radius.");
        
        Messages.sendTitle(player, "Near holograms");
        for (NamedHologram nearHologram : nearHolograms) {
            player.sendMessage(Colors.SECONDARY_SHADOW + "- " + Colors.SECONDARY + Colors.BOLD + nearHologram.getName() + " " + Colors.SECONDARY_SHADOW + "at x: " + (int) nearHologram.getX() + ", y: " + (int) nearHologram.getY() + ", z: " + (int) nearHologram.getZ() + " (lines: " + nearHologram.size() + ")");
        }
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Get a list of near holograms.");
    }

    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
