package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class SetlineCommand extends HologramSubCommand {

	public SetlineCommand() {
		super("setline");
		setPermission(Strings.BASE_PERM + "setline");
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
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[0].toLowerCase()));
		String line = Utils.join(args, " ", 2, args.length);
		
		// Check material validity
		if (line.toLowerCase().startsWith("icon:")) {
			String iconMaterial = ItemUtils.stripSpacingChars(line.substring("icon:".length(), line.length()));
			
			if (iconMaterial.contains(":")) {
				iconMaterial = iconMaterial.split(":")[0];
			}
					
			Material mat = ItemUtils.matchMaterial(iconMaterial);
			CommandValidator.notNull(mat, "Invalid icon material.");
		}
		
		int lineNumber = CommandValidator.getInteger(args[1]);
		CommandValidator.isTrue(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
		int index = lineNumber - 1;
		
		hologram.getLinesUnsafe().get(index).despawn();
		hologram.getLinesUnsafe().set(index, HologramDatabase.readLineFromString(line, hologram));
		hologram.refreshAll();

		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " changed!");
		Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
		
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
