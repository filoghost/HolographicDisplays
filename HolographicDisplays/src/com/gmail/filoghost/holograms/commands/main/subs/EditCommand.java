package com.gmail.filoghost.holograms.commands.main.subs;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.UNDERLINE;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.ChatColor.YELLOW;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.Format;
import com.gmail.filoghost.holograms.utils.ItemUtils;

public class EditCommand extends HologramSubCommand {

	public EditCommand() {
		super("edit");
		setPermission(Messages.MAIN_PERMISSION);
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
		String name = args[0].toLowerCase();
		CraftHologram hologram = HologramManager.getHologram(name);
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		
		sender.sendMessage("");
		sender.sendMessage(Format.formatTitle("How to edit the hologram '" + name + "'"));
		for (HologramSubCommand subCommand : HolographicDisplays.getInstance().getMainCommandHandler().getSubCommands()) {
			if (subCommand.getType() == SubCommandType.EDIT_LINES) {
				String usage = "/hd " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments().replace("<hologramName>", hologram.getName()).replace("<hologram>", hologram.getName()) : "");
				
				if (CommandValidator.isPlayerSender(sender)) {
					HolographicDisplays.nmsManager.newFancyMessage(usage)
						.color(AQUA)
						.suggest(usage)
						.itemTooltip(ItemUtils.getStone("§b" + usage, subCommand.getTutorial(), ChatColor.GRAY))
						.send((Player) sender);
				} else {
					sender.sendMessage("§b" + usage);
				}
			}
		}
		
		if (CommandValidator.isPlayerSender(sender) && HolographicDisplays.nmsManager.hasChatHoverFeature()) {
			sender.sendMessage("");
			HolographicDisplays.nmsManager.newFancyMessage("[").color(GOLD)
			.then("Tip").style(BOLD).color(YELLOW)
			.then("]").color(GOLD)
			.then(" Try to ").color(WHITE)
			.then("hover").color(WHITE).style(ITALIC, UNDERLINE)
			.tooltip("§dHover on the commands to get info about them.")
			.then(" or ")
			.then("click").color(WHITE).style(ITALIC, UNDERLINE)
			.tooltip("§dClick on the commands to insert them in the chat.")
			.then(" on the commands!")
			.send((Player) sender);
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Shows the commands to manipulate an existing hologram.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
