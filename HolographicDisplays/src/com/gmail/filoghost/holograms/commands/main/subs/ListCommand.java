package com.gmail.filoghost.holograms.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.Format;

public class ListCommand extends HologramSubCommand {

	private static final int HOLOGRAMS_PER_PAGE = 10;

	public ListCommand() {
		super("list");
		setPermission(Messages.MAIN_PERMISSION);
	}

	@Override
	public String getPossibleArguments() {
		return "[page]";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {

		int page = args.length > 0 ? CommandValidator.getInteger(args[0]) : 1;

		if (page < 1) {
			throw new CommandException("Page number must be 1 or greater.");
		}

		int totalPages = HologramManager.size() / HOLOGRAMS_PER_PAGE;
		if (HologramManager.size() % HOLOGRAMS_PER_PAGE != 0) {
			totalPages++;
		}
		
		
		if (HologramManager.size() == 0) {
			throw new CommandException("There are no holograms yet. Create one with /hd create.");
		}

		sender.sendMessage("");
		sender.sendMessage(Format.formatTitle("Holograms list §f(Page " + page + " of " + totalPages + ")"));
		int fromIndex = (page - 1) * HOLOGRAMS_PER_PAGE;
		int toIndex = fromIndex + HOLOGRAMS_PER_PAGE;

		for (int i = fromIndex; i < toIndex; i++) {
			if (i < HologramManager.size()) {
				CraftHologram hologram = HologramManager.get(i);
				sender.sendMessage("§3- §f'" + hologram.getName() + "' §7at x: " + hologram.getBlockX() + ", y: " + hologram.getBlockY() + ", z: " + hologram.getBlockZ() + " (" + hologram.getLinesLength() + " lines, world: \"" + hologram.getWorld().getName() + "\")");
			}
		}
		if (page < totalPages) {
			sender.sendMessage("§f[Tip] §7See the next page with /holograms list " + (page + 1));
		}

	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Lists all the existing holograms.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
