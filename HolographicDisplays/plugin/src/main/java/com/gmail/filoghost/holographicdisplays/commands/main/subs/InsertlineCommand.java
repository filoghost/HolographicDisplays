package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class InsertlineCommand extends HologramSubCommand {


	public InsertlineCommand() {
		super("insertline");
		setPermission(Strings.BASE_PERM + "insertline");
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
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[0].toLowerCase()));
		
		int insertAfter = CommandValidator.getInteger(args[1]);
		int oldLinesAmount = hologram.size();
		
		CommandValidator.isTrue(insertAfter >= 0 && insertAfter <= oldLinesAmount, "The number must be between 0 and " + hologram.size() + "(amount of lines of the hologram).");

		hologram.getLinesUnsafe().add(insertAfter, HologramDatabase.readLineFromString(Utils.join(args, " ", 2, args.length), hologram));
		hologram.refreshAll();
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		
		if (insertAfter == 0) {
			sender.sendMessage(Colors.PRIMARY + "Line inserted before line n.1!");
		} else if (insertAfter == oldLinesAmount) {
			sender.sendMessage(Colors.PRIMARY + "Line appended at the end!");
			sender.sendMessage(Strings.TIP_PREFIX + "Next time use /" + label + " addline to add a line at the end.");
		} else {
			sender.sendMessage(Colors.PRIMARY + "Line inserted between lines " + insertAfter + " and " + (insertAfter + 1) + "!");
		}
		Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
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
