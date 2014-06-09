package com.gmail.filoghost.holograms.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.database.HologramDatabase;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.Format;

public class RemovelineCommand extends HologramSubCommand {

	public RemovelineCommand() {
		super("removeline");
		setPermission(Messages.MAIN_PERMISSION);
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> <lineNumber>";
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}


	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		
		int lineNumber = CommandValidator.getInteger(args[1]);

		CommandValidator.isTrue(lineNumber >= 1 && lineNumber <= hologram.getLinesLength(), "The line number must be between 1 and " + hologram.getLinesLength() + ".");
		int index = lineNumber - 1;
		
		CommandValidator.isTrue(hologram.getLinesLength() > 1, "The hologram should have at least 1 line.");

		hologram.removeLine(index);
		
		if (!hologram.update()) {
			sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}
		
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Format.HIGHLIGHT + "Line " + lineNumber + " removed!");
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Removes a line from a hologram.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.EDIT_LINES;
	}

}
