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

public class RemovelineCommand extends HologramSubCommand {

	public RemovelineCommand() {
		super("removeline");
		setPermission(Strings.BASE_PERM + "removeline");
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
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[0].toLowerCase()));
		
		int lineNumber = CommandValidator.getInteger(args[1]);

		CommandValidator.isTrue(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
		int index = lineNumber - 1;
		
		CommandValidator.isTrue(hologram.size() > 1, "The hologram should have at least 1 line. If you want to delete it, use /" + label + " delete.");

		hologram.removeLine(index);
		hologram.refreshAll();
		
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " removed!");
		Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
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
