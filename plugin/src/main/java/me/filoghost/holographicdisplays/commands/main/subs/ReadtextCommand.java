/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.Colors;
import me.filoghost.holographicdisplays.Permissions;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Messages;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.common.Utils;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.disk.HologramLineParser;
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.exception.HologramLineParseException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.util.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadtextCommand extends HologramSubCommand {

    public ReadtextCommand() {
        super("readtext", "readlines");
        setPermission(Permissions.COMMAND_BASE + "readtext");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> <fileWithExtension>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        String fileName = args[1];
        
        try {
            File targetFile = CommandValidator.getUserReadableFile(fileName);
            List<String> serializedLines = FileUtils.readLines(targetFile);
            
            int linesAmount = serializedLines.size();
            if (linesAmount > 40) {
                Messages.sendWarning(sender, "The file contained more than 40 lines, that have been limited.");
                linesAmount = 40;
            }
            
            List<CraftHologramLine> linesToAdd = new ArrayList<>();
            for (int i = 0; i < linesAmount; i++) {
                try {
                    CraftHologramLine line = HologramLineParser.parseLine(hologram, serializedLines.get(i), true);
                    linesToAdd.add(line);
                } catch (HologramLineParseException e) {
                    throw new CommandException("Error at line " + (i + 1) + ": " + Utils.uncapitalize(e.getMessage()));
                }
            }
            
            hologram.clearLines();
            hologram.getLinesUnsafe().addAll(linesToAdd);
            hologram.refreshAll();

            HologramDatabase.saveHologram(hologram);
            HologramDatabase.trySaveToDisk();
            
            if (args[1].contains(".")) {
                if (isImageExtension(args[1].substring(args[1].lastIndexOf('.') + 1))) {
                    Messages.sendWarning(sender, "The read file has an image's extension. If it is an image, you should use /" + label + " readimage.");
                }
            }
            
            sender.sendMessage(Colors.PRIMARY + "The lines were pasted into the hologram!");
            Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
            
        } catch (FileNotFoundException e) {
            throw new CommandException("A file named '" + args[1] + "' doesn't exist in the plugin's folder.");
        } catch (IOException e) {
            throw new CommandException("I/O exception while reading the file. Is it in use?");
        }
    }
    
    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Reads the lines from a text file. Tutorial:",
            "1) Create a new text file in the plugin's folder",
            "2) Do not use spaces in the name",
            "3) Each line will be a line in the hologram",
            "4) Do /holograms readlines <hologramName> <fileWithExtension>",
            "",
            "Example: you have a file named 'info.txt', and you want",
            "to paste it in the hologram named 'test'. In this case you",
            "would execute "+ ChatColor.YELLOW + "/holograms readlines test info.txt");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.EDIT_LINES;
    }

    private boolean isImageExtension(String input) {
        return Arrays.asList("jpg", "png", "jpeg", "gif").contains(input.toLowerCase());
    }
    
}
