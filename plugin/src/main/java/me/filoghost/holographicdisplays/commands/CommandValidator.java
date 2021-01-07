/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.disk.HologramLineParser;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.exception.HologramLineParseException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.util.FileUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        notNull(hologram, "Cannot find a hologram named \"" + hologramName + "\".");
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

    public static Path getUserReadableFile(String fileName) throws CommandException, IOException {
        Path dataFolder = HolographicDisplays.getDataFolderPath();
        Path targetFile = dataFolder.resolve(fileName);
        CommandValidator.isTrue(FileUtils.isInsideDirectory(targetFile, dataFolder), "The specified file must be inside HolographicDisplays' folder.");
        CommandValidator.isTrue(Files.exists(targetFile), "The specified file \"" + fileName + "\" does not exist inside HolographicDisplays' folder.");
        CommandValidator.isTrue(!Files.isDirectory(targetFile), "The file cannot be a folder.");
        CommandValidator.isTrue(!isConfigFile(targetFile), "Cannot read YML configuration files.");
        return targetFile;
    }

    private static boolean isConfigFile(Path file) {
        return Files.isRegularFile(file) && file.getFileName().toString().toLowerCase().endsWith(".yml");
    }

}
