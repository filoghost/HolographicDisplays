/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.commands.subs;

import me.filoghost.fcommons.Colors;
import me.filoghost.fcommons.command.CommandContext;
import me.filoghost.fcommons.command.sub.SubCommandContext;
import me.filoghost.fcommons.command.validation.CommandException;
import me.filoghost.fcommons.command.validation.CommandValidate;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.commands.InternalHologramEditor;
import me.filoghost.holographicdisplays.plugin.event.InternalHologramChangeEvent.ChangeType;
import me.filoghost.holographicdisplays.plugin.format.ColorScheme;
import me.filoghost.holographicdisplays.plugin.format.DisplayFormat;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalTextLine;
import me.filoghost.holographicdisplays.plugin.image.ImageMessage;
import me.filoghost.holographicdisplays.plugin.image.ImageReadException;
import me.filoghost.holographicdisplays.plugin.image.ImageReader;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ReadimageCommand extends LineEditingCommand {

    private final InternalHologramEditor hologramEditor;

    public ReadimageCommand(InternalHologramEditor hologramEditor) {
        super("readimage", "image");
        setMinArgs(3);
        setUsageArgs("<hologram> <imageWithExtension> <width>");

        this.hologramEditor = hologramEditor;
    }

    @Override
    public List<String> getDescription(CommandContext context) {
        return Arrays.asList(
                "Reads an image from a file. Tutorial:",
                "1) Move the image in the plugin's folder",
                "2) Do not use spaces in the name",
                "3) Do " + getFullUsageText(context),
                "4) Choose <width> to automatically resize the image",
                "5) (Optional) Use the flag '-a' if you only want to append",
                "   the image to the hologram without clearing the lines",
                "",
                "Example: you have an image named 'logo.png', you want to append",
                "it to the lines of the hologram named 'test', with a width of",
                "50 pixels. In this case you would execute the following command:",
                ChatColor.YELLOW + "/" + context.getRootLabel() + " " + getName() + " test logo.png 50 -a",
                "",
                "The symbols used to create the image are taken from the config.yml.");
    }

    @Override
    public void execute(CommandSender sender, String[] args, SubCommandContext context) throws CommandException {
        List<String> newArgs = new ArrayList<>(Arrays.asList(args));
        boolean append = extractAppendFlag(newArgs);
        args = newArgs.toArray(new String[0]);

        InternalHologram hologram = hologramEditor.getHologram(args[0]);

        int width = CommandValidate.parseInteger(args[2]);
        CommandValidate.check(width >= 2, "The width of the image must be 2 or greater.");
        CommandValidate.check(width <= 150, "The width of the image must be 150 or lower.");

        boolean isUrl = false;

        String fileName = args[1];
        BufferedImage image;

        try {
            if (fileName.startsWith("http://") || fileName.startsWith("https://")) {
                isUrl = true;
                image = ImageReader.readImage(new URL(fileName));
            } else {
                if (fileName.matches(".*[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-]{1,4}/.+")) {
                    DisplayFormat.sendWarning(sender, "The image path seems to be an URL. If so, please use the http[s]:// prefix.");
                }

                Path targetImage = hologramEditor.getUserReadableFile(fileName);
                image = ImageReader.readImage(targetImage);
            }
        } catch (MalformedURLException e) {
            throw new CommandException("The provided URL was not valid.");
        } catch (ImageReadException e) {
            throw new CommandException("The plugin was unable to read the image. Be sure that the format is supported.");
        } catch (IOException e) {
            Log.warning("Error while reading an image", e);
            throw new CommandException("I/O exception while reading the image. " + (isUrl ? "Is the URL valid?" : "Is it in use?"));
        }

        ImageMessage imageMessage = new ImageMessage(image, width);
        List<InternalTextLine> newLines = new ArrayList<>();
        for (String newLine : imageMessage.getLines()) {
            newLines.add(hologram.createTextLine(newLine, Colors.uncolorize(newLine)));
        }

        if (newLines.size() < 5) {
            DisplayFormat.sendTip(sender, "The image has a very low height."
                    + " You can increase it by increasing the width, it will scale automatically.");
        }

        if (!append) {
            hologram.clearLines();
        }
        hologram.addLines(newLines);
        hologramEditor.saveChanges(hologram, ChangeType.EDIT_LINES);

        if (append) {
            sender.sendMessage(ColorScheme.PRIMARY + "The image was appended int the end of the hologram.");
        } else {
            sender.sendMessage(ColorScheme.PRIMARY + "The image was drawn in the hologram.");
        }
    }

    private boolean extractAppendFlag(List<String> args) {
        Iterator<String> iterator = args.iterator();

        while (iterator.hasNext()) {
            String arg = iterator.next();
            if (arg.equalsIgnoreCase("-a") || arg.equalsIgnoreCase("-append")) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

}
