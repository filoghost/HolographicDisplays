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
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;

public class InfoCommand extends HologramSubCommand {

	public InfoCommand() {
		super("info", "details");
		setPermission(Strings.BASE_PERM + "info");
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
		String name = args[0].toLowerCase();
		NamedHologram hologram = NamedHologramManager.getHologram(name);
		CommandValidator.notNull(hologram, Strings.noSuchHologram(name));
		
		sender.sendMessage("");
		sender.sendMessage(Strings.formatTitle("Lines of the hologram '" + name + "'"));
		int index = 0;
		
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			sender.sendMessage(Colors.SECONDARY + Colors.BOLD + (++index) + Colors.SECONDARY_SHADOW + ". " + Colors.SECONDARY + (line instanceof CraftTextLine ? ((CraftTextLine) line).getText() : HologramDatabase.saveLineToString(line)));
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Shows the lines of a hologram.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.EDIT_LINES;
	}

}
