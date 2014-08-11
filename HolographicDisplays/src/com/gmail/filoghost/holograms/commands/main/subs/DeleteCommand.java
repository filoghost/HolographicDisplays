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

public class DeleteCommand extends HologramSubCommand {

	public DeleteCommand() {
		super("delete", "remove");
		setPermission(Messages.BASE_PERM + "delete");
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
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		hologram.hide();
		HologramManager.remove(hologram);
		HologramDatabase.deleteHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Format.HIGHLIGHT + "You deleted the hologram '" + hologram.getName() + "'.");
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
