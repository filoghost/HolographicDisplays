package com.gmail.filoghost.holograms.commands.main.subs;

import static org.bukkit.ChatColor.*;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.utils.Format;
import com.gmail.filoghost.holograms.utils.ItemUtils;

public class HelpCommand extends HologramSubCommand {


	public HelpCommand() {
		super("help");
		setPermission(Messages.MAIN_PERMISSION);
	}

	@Override
	public String getPossibleArguments() {
		return "";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}


	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		sender.sendMessage("");
		sender.sendMessage(Format.formatTitle("Holographic Displays Commands"));
		for (HologramSubCommand subCommand : HolographicDisplays.getInstance().getMainCommandHandler().getSubCommands()) {
			if (subCommand.getType() == SubCommandType.GENERIC) {
				String usage = "/hd " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments() : "");
				
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
		return null;
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.HIDDEN;
	}


}
