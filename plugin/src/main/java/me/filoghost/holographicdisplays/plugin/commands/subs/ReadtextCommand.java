/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.command.CommandContext;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.config.HologramLineParser;
import me.filoghost.holographicdisplays.plugin.config.HologramLoadException;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.util.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadtextCommand extends LineEditingCommand {

    private final InternalHologramEditor hologramEditor;

    public ReadtextCommand(InternalHologramEditor hologramEditor) {
        super("readtext", "readlines");
        setMinArgs(2);
        setUsageArgs("<hologram> <fileWithExtension>");

        this.hologramEditor = hologramEditor;
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
        InternalHologram hologram = hologramEditor.getHologram(args[0]);
        String fileName = args[1];

        Path fileToRead = hologramEditor.getUserReadableFile(fileName);
        List<String> serializedLines;

        try {
            serializedLines = Files.readAllLines(fileToRead);
        } catch (IOException e) {
            throw new CommandException("I/O exception while reading the file. Is it in use?");
        }

        int linesAmount = serializedLines.size();
        if (linesAmount > 40) {
            DisplayFormat.sendWarning(sender, "The file contained more than 40 lines, that have been limited.");
            linesAmount = 40;
        }

        List<InternalHologramLine> newLines = new ArrayList<>();
        for (int i = 0; i < linesAmount; i++) {
            try {
                InternalHologramLine line = HologramLineParser.parseLine(hologram, serializedLines.get(i));
                newLines.add(line);
            } catch (HologramLoadException e) {
                throw new CommandException("Error at line " + (i + 1) + ": " + e.getMessage());
            }
        }

        hologram.getLines().setAll(newLines);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LINES);

        if (FileUtils.hasFileExtension(fileToRead, "jpg", "png", "jpeg", "gif")) {
            DisplayFormat.sendWarning(sender, "The read file has an image's extension."
                    + " If it is an image, you should use /" + context.getRootLabel() + " readimage.");
        }

        sender.sendMessage(ColorScheme.PRIMARY + "The lines were pasted into the hologram.");
    }

}
