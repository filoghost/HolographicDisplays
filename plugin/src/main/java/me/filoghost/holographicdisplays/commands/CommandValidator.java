/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands;

import me.filoghost.holographicdisplays.disk.HologramLineParser;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.exception.HologramLineParseException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandValidator {
    
    public static CraftHologramLine parseHologramLine(NamedHologram hologram, String serializedLine, boolean validateMaterial) throws CommandException {
        try {
            return HologramLineParser.parseLine(hologram, serializedLine, validateMaterial);
        } catch (HologramLineParseException e) {
            throw new CommandException(e.getMessage());
        }
    }
    
    public static NamedHologram getNamedHologram(String hologramName) throws CommandException {
        NamedHologram hologram = NamedHologramManager.getHologram(hologramName);
        notNull(hologram, Strings.noSuchHologram(hologramName));
        return hologram;
    }
    
    public static void notNull(Object obj, String string) throws CommandException {
        if (obj == null) {
            throw new CommandException(string);
        }
    }
    
    public static void isTrue(boolean b, String string) throws CommandException {
        if (!b) {
            throw new CommandException(string);
        }
    }

    public static int getInteger(String integer) throws CommandException {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException ex) {
            throw new CommandException("Invalid number: '" + integer + "'.");
        }
    }
    
    public static Player getPlayerSender(CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            throw new CommandException("You must be a player to use this command.");
        }
    }
    
    public static boolean isPlayerSender(CommandSender sender) {
        return sender instanceof Player;
    }
    
}