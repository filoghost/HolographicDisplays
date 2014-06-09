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

public class InsertlineCommand extends HologramSubCommand {


	public InsertlineCommand() {
		super("insertline");
		setPermission(Messages.MAIN_PERMISSION);
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> <lineNumber> <text>";
	}

	@Override
	public int getMinimumArguments() {
		return 3;
	}


	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		
		int insertAfter = CommandValidator.getInteger(args[1]);
		int oldLinesAmount = hologram.getLinesLength();
		
		CommandValidator.isTrue(insertAfter >= 0 && insertAfter <= oldLinesAmount, "The number must be between 0 and " + hologram.getLinesLength() + "(amount of lines of the hologram).");

		if (args[2].equalsIgnoreCase("{empty}")) {
			hologram.insertLine(insertAfter, "");
		} else {
			hologram.insertLine(insertAfter, StringUtils.toReadableFormat(StringUtils.join(args, " ", 2, args.length)));
		}
		
		if (!hologram.update()) {
			sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		if (insertAfter == 0) {
			sender.sendMessage(Format.HIGHLIGHT + "Line inserted before line n.1!");
		} else if (insertAfter == oldLinesAmount) {
			sender.sendMessage(Format.HIGHLIGHT + "Line appended at the end!");
			sender.sendMessage("§7[Tip] Next time use /hd addline to add a line at the end.");
		} else {
			sender.sendMessage(Format.HIGHLIGHT + "Line inserted between lines " + insertAfter + " and " + (insertAfter+1) + "!");
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Inserts a line after the specified index.",
							 "If the index is 0, the line will be put before",
							 "the first line of the hologram.");
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.EDIT_LINES;
	}

}
