/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.subs;

import me.filoghost.fcommons.command.CommandContext;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.commands.HologramCommandValidate;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.disk.ConfigManager;
import me.filoghost.holographicdisplays.disk.HologramLineParser;
import me.filoghost.holographicdisplays.disk.HologramLoadException;
import me.filoghost.holographicdisplays.event.InternalHologramEditEvent;
import me.filoghost.holographicdisplays.object.internal.InternalHologram;
import me.filoghost.holographicdisplays.object.internal.InternalHologramLine;
import me.filoghost.holographicdisplays.object.internal.InternalHologramManager;
import me.filoghost.holographicdisplays.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadtextCommand extends LineEditingCommand {

    private final InternalHologramManager internalHologramManager;
    private final ConfigManager configManager;
    
    public ReadtextCommand(InternalHologramManager internalHologramManager, ConfigManager configManager) {
        super("readtext", "readlines");
        setMinArgs(2);
        setUsageArgs("<hologram> <fileWithExtension>");

        this.internalHologramManager = internalHologramManager;
        this.configManager = configManager;
    }

    @Override
    public List<String> getDescription(CommandContext context) {
        return Arrays.asList(
                "Reads the lines from a text file. Tutorial:",
                "1) Create a new text file in the plugin's folder",
                "2) Do not use spaces in the name",
                "3) Each line will be a line in the hologram",
                "4) Do " + getFullUsageText(context),
                "",
                "Example: you have a file named 'info.txt', and you want",
                "to paste it in the hologram named 'test'. In this case you",
                "would execute " + ChatColor.YELLOW + "/" + context.getRootLabel() + " " + getName() + " test info.txt");
    }
    
    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        InternalHologram hologram = HologramCommandValidate.getNamedHologram(internalHologramManager, args[0]);
        String fileName = args[1];
        
        try {
            Path targetFile = HologramCommandValidate.getUserReadableFile(configManager.getRootDataFolder(), fileName);
            List<String> serializedLines = Files.readAllLines(targetFile);
            
            int linesAmount = serializedLines.size();
            if (linesAmount > 40) {
                Messages.sendWarning(sender, "The file contained more than 40 lines, that have been limited.");
                linesAmount = 40;
            }
            
            List<InternalHologramLine> linesToAdd = new ArrayList<>();
            for (int i = 0; i < linesAmount; i++) {
                try {
                    InternalHologramLine line = HologramLineParser.parseLine(hologram, serializedLines.get(i), true);
                    linesToAdd.add(line);
                } catch (HologramLoadException e) {
                    throw new CommandException("Error at line " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            hologram.clearLines();
            hologram.getLinesUnsafe().addAll(linesToAdd);
            hologram.refresh();

            configManager.getHologramDatabase().addOrUpdate(hologram);
            configManager.saveHologramDatabase();
            
            if (isImageExtension(FileUtils.getExtension(fileName))) {
                Messages.sendWarning(sender, "The read file has an image's extension. If it is an image, you should use /" + context.getRootLabel() + " readimage.");
            }
            
            sender.sendMessage(Colors.PRIMARY + "The lines were pasted into the hologram.");
            Bukkit.getPluginManager().callEvent(new InternalHologramEditEvent(hologram));
            
        } catch (IOException e) {
            throw new CommandException("I/O exception while reading the file. Is it in use?");
        }
    }
    
    private boolean isImageExtension(String extension) {
        return Arrays.asList("jpg", "png", "jpeg", "gif").contains(extension.toLowerCase());
    }
    
}
