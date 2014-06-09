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
import com.gmail.filoghost.holograms.utils.StringUtils;

public class SetlineCommand extends HologramSubCommand {

	public SetlineCommand() {
		super("setline");
		setPermission(Messages.MAIN_PERMISSION);
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> <lineNumber> <newText>";
	}

	@Override
	public int getMinimumArguments() {
		return 3;
	}


	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		
			
		int lineNumber = CommandValidator.getInteger(args[1]);
		CommandValidator.isTrue(lineNumber >= 1 && lineNumber <= hologram.getLinesLength(), "The line number must be between 1 and " + hologram.getLinesLength() + ".");
		int index = lineNumber - 1;
		
		if (args[2].equalsIgnoreCase("{empty}")) {
			hologram.setLine(index, "");
		} else {
			hologram.setLine(index, StringUtils.toReadableFormat(org.apache.commons.lang.StringUtils.join(args, " ", 2, args.length)));
		}
			
		if (!hologram.update()) {
			sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}

		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Format.HIGHLIGHT + "Line " + lineNumber + " changed!");
		
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Changes a line of a hologram.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.EDIT_LINES;
	}

}
