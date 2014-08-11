package com.gmail.filoghost.holograms.commands.main.subs;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.database.HologramDatabase;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.exception.TooWideException;
import com.gmail.filoghost.holograms.exception.UnreadableImageException;
import com.gmail.filoghost.holograms.image.ImageMessage;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.FileUtils;
import com.gmail.filoghost.holograms.utils.Format;

public class ReadimageCommand extends HologramSubCommand {


	public ReadimageCommand() {
		super("readimage", "image");
		setPermission(Messages.BASE_PERM + "readimage");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologram> <imageWithExtension> <width>";
	}

	@Override
	public int getMinimumArguments() {
		return 3;
	}


	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		
		int width = CommandValidator.getInteger(args[2]);
		
		CommandValidator.isTrue(width >= 3, "The width of the image must be 3 or greater.");

		try {
			
			BufferedImage image = FileUtils.readImage(args[1]);
			hologram.clearLines();
			
			ImageMessage imageMessage = new ImageMessage(image, width);
			String[] newLines = imageMessage.getLines();
			for (int i = 0; i < newLines.length; i++) {
				hologram.addLine(newLines[i]);
			}
			
			if (!hologram.update()) {
				sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
			}
			
			if (newLines.length < 5) {
				sender.sendMessage("§6[§eTip§6] §fSeems that the image has a very low height. You can increase it by increasing the width.");
			}
			
			HologramDatabase.saveHologram(hologram);
			HologramDatabase.trySaveToDisk();
			sender.sendMessage(Format.HIGHLIGHT + "The image was drawn in the hologram!");
			
		} catch (FileNotFoundException e) {
			throw new CommandException("The image '" + args[1] + "' doesn't exist in the plugin's folder.");
		} catch (TooWideException e) {
			throw new CommandException("The image is too large. Max width allowed is 100 pixels.");
		} catch (UnreadableImageException e) {
			throw new CommandException("The plugin was unable to read the image. Be sure that the format is supported.");
		} catch (IOException e) {
			throw new CommandException("I/O exception while reading the image. Is it in use?");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommandException("Unhandled exception while reading the image! Please look the console.");
		}
	}
	
	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Reads an image from a file. §fTutorial:",
				"1) Move the image in the plugin's folder",
				"2) Do not use spaces in the name",
				"3) Do /hd read <hologram> <image> <width>",
				"4) Choose <width> to automatically resize the image",
				"",
				"§fExample: §7you have an image named §f'logo.png'§7, you want",
				"to paste it in the hologram named §f'test'§7, with a width of 50",
				"pixels. In this case you would execute the following command:",
				"§e/hd readimage test logo.png 50",
				"",
				"The symbols used to create the image are taken from the config.yml.",
				"",
				"§c§l§nNOTE:§f Do not use big images, as they can cause lag to clients.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.EDIT_LINES;
	}

}
