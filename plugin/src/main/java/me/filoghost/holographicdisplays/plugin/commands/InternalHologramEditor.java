/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.holographicdisplays.plugin.disk.ConfigManager;
import me.filoghost.holographicdisplays.plugin.disk.HologramLineParser;
import me.filoghost.holographicdisplays.plugin.disk.HologramLoadException;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import me.filoghost.holographicdisplays.plugin.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InternalHologramEditor {

    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;

    public InternalHologramEditor(InternalHologramManager internalHologramManager, ConfigManager configManager) {
        this.internalHologramManager = internalHologramManager;
        this.configManager = configManager;
    }

    public InternalHologramLine parseHologramLine(InternalHologram hologram, String serializedLine) throws CommandException {
        try {
            return HologramLineParser.parseLine(hologram, serializedLine);
        } catch (HologramLoadException e) {
            throw new CommandException(formatExceptionMessage(e));
        }
    }

    private String formatExceptionMessage(Throwable throwable) {
        String message = throwable.getMessage();
        if (Strings.isEmpty(message)) {
            return message;
        }

        message = Strings.capitalizeFirst(message);
        if (!Strings.hasSentenceEnding(message)) {
            message = message + ".";
        }
        return message;
    }

    public InternalHologram getHologram(String hologramName) throws CommandException {
        InternalHologram hologram = internalHologramManager.getHologramByName(hologramName);
        CommandValidate.notNull(hologram, "Cannot find a hologram named \"" + hologramName + "\".");
        return hologram;
    }

    public List<InternalHologram> getHolograms() {
        return internalHologramManager.getHolograms();
    }

    public InternalHologram create(Location spawnLocation, String hologramName) {
        return internalHologramManager.createHologram(spawnLocation, hologramName);
    }

    public void delete(InternalHologram hologram) {
        internalHologramManager.deleteHologram(hologram);
    }

    public void saveChanges(InternalHologram hologram, ChangeType changeType) {
        configManager.saveHologramDatabase(internalHologramManager);
        Bukkit.getPluginManager().callEvent(new InternalHologramChangeEvent(hologram, changeType));
    }

    public void teleportLookingDown(Player player, Location location) {
        location.setPitch(90); // Look down
        player.teleport(location, TeleportCause.PLUGIN);
    }

    public Path getUserReadableFile(String fileName) throws CommandException {
        Path dataFolder = configManager.getRootDataFolder();
        Path targetFile = dataFolder.resolve(fileName);

        CommandValidate.check(FileUtils.isInsideDirectory(targetFile, dataFolder),
                "The specified file must be inside HolographicDisplays' folder.");
        CommandValidate.check(Files.exists(targetFile),
                "The specified file \"" + fileName + "\" does not exist inside HolographicDisplays' folder.");
        CommandValidate.check(!Files.isDirectory(targetFile), "The file cannot be a folder.");
        CommandValidate.check(!isConfigFile(targetFile), "Cannot read YML configuration files.");
        return targetFile;
    }

    private static boolean isConfigFile(Path file) {
        return Files.isRegularFile(file) && file.getFileName().toString().toLowerCase().endsWith(".yml");
    }

}
