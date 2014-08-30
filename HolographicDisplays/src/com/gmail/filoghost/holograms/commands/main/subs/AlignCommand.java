package com.gmail.filoghost.holograms.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.database.HologramDatabase;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.Format;

public class AlignCommand extends HologramSubCommand {

	public AlignCommand() {
		super("align");
		setPermission(Messages.BASE_PERM + "align");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramToAlign> <referenceHologram>";
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CraftHologram referenceHologram = HologramManager.getHologram(args[1].toLowerCase());
		
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM + " (hologram to align)");
		CommandValidator.notNull(referenceHologram, Messages.NO_SUCH_HOLOGRAM + " (reference hologram)");
		
		CommandValidator.isTrue(hologram != referenceHologram, "The hologram must not be the same!");

		Location loc = hologram.getLocation();
		loc.setY(referenceHologram.getY());
		hologram.setLocation(loc);
		
		
		if (!hologram.update()) {
			sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Format.HIGHLIGHT + "Hologram \"" + hologram.getName() + "\" vertically aligned to the hologram \"" + referenceHologram.getName() + "\".");
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Vertically aligns the first hologram to the second.");
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
