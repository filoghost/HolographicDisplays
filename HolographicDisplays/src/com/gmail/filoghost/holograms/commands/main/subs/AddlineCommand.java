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

public class AddlineCommand extends HologramSubCommand {

	public AddlineCommand() {
		super("addline");
		setPermission(Messages.MAIN_PERMISSION);
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> <text>";
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);

		if (args[1].equalsIgnoreCase("{empty}")) {
			hologram.addLine("");
		} else {
			hologram.addLine(StringUtils.toReadableFormat(StringUtils.join(args, " ", 1, args.length)));
		}
		if (!hologram.update()) {
			sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Format.HIGHLIGHT + "Line added!");
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Adds a line to an existing hologram.");
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.EDIT_LINES;
	}

}
