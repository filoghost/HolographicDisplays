package com.gmail.filoghost.holograms.commands.main.subs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.database.HologramDatabase;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.utils.Format;

public class SaveCommand extends HologramSubCommand {

	public SaveCommand() {
		super("save");
		setPermission(Messages.BASE_PERM + "save");
	}

	@Override
	public String getPossibleArguments() {
		return "";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}


	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		try {
			HologramDatabase.saveToDisk();
			sender.sendMessage(Format.HIGHLIGHT + "Holograms saved!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CommandException("Unable to save holograms to database.yml! Was the file in use? Look the console for more info.");
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Saves all the holograms to the database. You usually",
							 "don't need to use this command, because the holograms",
							 "are automatically saved to disk every 10 minutes.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
