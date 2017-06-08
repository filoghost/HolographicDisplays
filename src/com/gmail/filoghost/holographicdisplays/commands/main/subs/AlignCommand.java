package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;

public class AlignCommand extends HologramSubCommand {

	public AlignCommand() {
		super("align");
		setPermission(Strings.BASE_PERM + "align");
	}

	@Override
	public String getPossibleArguments() {
		return "<X | Y | Z | XZ> <hologram> <referenceHologram>";
	}

	@Override
	public int getMinimumArguments() {
		return 3;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		NamedHologram hologram = NamedHologramManager.getHologram(args[1].toLowerCase());
		NamedHologram referenceHologram = NamedHologramManager.getHologram(args[2].toLowerCase());
		
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[1].toLowerCase()));
		CommandValidator.notNull(referenceHologram, Strings.noSuchHologram(args[2].toLowerCase()));
		
		CommandValidator.isTrue(hologram != referenceHologram, "The hologram must not be the same!");

		Location loc = hologram.getLocation();
		
		if (args[0].equalsIgnoreCase("x")) {
			loc.setX(referenceHologram.getX());
		} else if (args[0].equalsIgnoreCase("y")) {
			loc.setY(referenceHologram.getY());
		} else if (args[0].equalsIgnoreCase("z")) {
			loc.setZ(referenceHologram.getZ());
		} else if (args[0].equalsIgnoreCase("xz")) {
			loc.setX(referenceHologram.getX());
			loc.setZ(referenceHologram.getZ());
		} else {
			throw new CommandException("You must specify either X, Y, Z or XZ, " + args[0] + " is not a valid axis.");
		}

		hologram.teleport(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
		hologram.despawnEntities();
		hologram.refreshAll();
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Colors.PRIMARY + "Hologram \"" + hologram.getName() + "\" aligned to the hologram \"" + referenceHologram.getName() + "\" on the " + args[0].toUpperCase() + " axis.");
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Aligns the first hologram to the second, in the specified axis.");
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
