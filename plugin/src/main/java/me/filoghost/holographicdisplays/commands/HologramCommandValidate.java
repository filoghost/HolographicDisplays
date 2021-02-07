/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands;

import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.HologramLineParser;
import me.filoghost.holographicdisplays.disk.HologramLineParseException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class HologramCommandValidate {
    
    public static CraftHologramLine parseHologramLine(NamedHologram hologram, String serializedLine, boolean validateMaterial) throws CommandException {
        try {
            return HologramLineParser.parseLine(hologram, serializedLine, validateMaterial);
        } catch (HologramLineParseException e) {
            throw new CommandException(Utils.formatExceptionMessage(e));
        }
    }

    public static NamedHologram getNamedHologram(String hologramName) throws CommandException {
        NamedHologram hologram = NamedHologramManager.getHologram(hologramName);
        CommandValidate.notNull(hologram, "Cannot find a hologram named \"" + hologramName + "\".");
        return hologram;
    }

    public static Path getUserReadableFile(String fileName) throws CommandException {
        Path dataFolder = HolographicDisplays.getDataFolderPath();
        Path targetFile = dataFolder.resolve(fileName);
        CommandValidate.check(FileUtils.isInsideDirectory(targetFile, dataFolder), "The specified file must be inside HolographicDisplays' folder.");
        CommandValidate.check(Files.exists(targetFile), "The specified file \"" + fileName + "\" does not exist inside HolographicDisplays' folder.");
        CommandValidate.check(!Files.isDirectory(targetFile), "The file cannot be a folder.");
        CommandValidate.check(!isConfigFile(targetFile), "Cannot read YML configuration files.");
        return targetFile;
    }

    private static boolean isConfigFile(Path file) {
        return Files.isRegularFile(file) && file.getFileName().toString().toLowerCase().endsWith(".yml");
    }

}
