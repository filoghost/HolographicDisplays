package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramsCommandHandler;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.util.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

public class HelpCommand extends HologramSubCommand {
	
	private HologramsCommandHandler mainCommandHandler;

	public HelpCommand(HologramsCommandHandler mainCommandHandler) {
		super("help");
		setPermission(Strings.BASE_PERM + "help");
		this.mainCommandHandler = mainCommandHandler;
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
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		sender.sendMessage("");
		sender.sendMessage(Strings.formatTitle("Holographic Displays Commands"));
		for (HologramSubCommand subCommand : mainCommandHandler.getSubCommands()) {
			if (subCommand.getType() == SubCommandType.GENERIC) {
				String usage = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments() : "");
				
				if (CommandValidator.isPlayerSender(sender)) {
					
					List<String> help = Utils.newList();
					help.add(Colors.PRIMARY + usage);
					for (String tutLine : subCommand.getTutorial()) {
						help.add(Colors.SECONDARY_SHADOW + tutLine);
					}
					
					((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
						.color(ChatColor.AQUA)
						.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Utils.join(help, "\n"))))
						.create());
					
				} else {
					sender.sendMessage(Colors.PRIMARY + usage);
				}
			}
		}
		
		if (CommandValidator.isPlayerSender(sender)) {
			sendHoverTip((Player) sender);
		}
	}
	
	public static void sendHoverTip(Player player) {
		player.sendMessage("");
		player.spigot().sendMessage(new ComponentBuilder("TIP").color(ChatColor.YELLOW).bold(true)
			.append(" Try to ", FormatRetention.NONE).color(ChatColor.GRAY)
			.append("hover").color(ChatColor.WHITE).italic(true).underlined(true)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Hover on the commands to get info about them.")))
			.append(" or ", FormatRetention.NONE).color(ChatColor.GRAY)
			.append("click").color(ChatColor.WHITE).italic(true).underlined(true)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Click on the commands to insert them in the chat.")))
			.append(" on the commands!", FormatRetention.NONE).color(ChatColor.GRAY)
			.create());
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
