package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;

public class DeleteCommand extends HologramSubCommand {

	public DeleteCommand() {
		super("delete", "remove");
		setPermission(Strings.BASE_PERM + "delete");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName>";
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}



	@Override
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[0].toLowerCase()));
		
		hologram.delete();
		NamedHologramManager.removeHologram(hologram);
		HologramDatabase.deleteHologram(hologram.getName());
		
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Colors.PRIMARY + "You deleted the hologram '" + hologram.getName() + "'.");
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Deletes a hologram. Cannot be undone.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
