package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.List;

import com.gmail.filoghost.holographicdisplays.util.message.FancyComponent;
import com.gmail.filoghost.holographicdisplays.util.message.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramsCommandHandler;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.util.Utils;

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

					HolographicDisplays.getNMSManager().sendFancyMessage(FancyMessage.builder()
							.component(
									FancyComponent.builder()
											.text(usage)
											.color(ChatColor.AQUA)
											.suggest(usage)
											.tooltip(help)
											.build()
							).build(), (Player) sender);
				} else {
					sender.sendMessage(Colors.PRIMARY + usage);
				}
			}
		}

		if (CommandValidator.isPlayerSender(sender)) {
			sendHoverTip(sender);
		}
	}

	public static void sendHoverTip(CommandSender sender) {
		sender.sendMessage("");
		FancyMessage message = FancyMessage.builder()
				.component(FancyComponent.builder().text("TIP").style(ChatColor.BOLD).color(ChatColor.YELLOW).build())
				.component(FancyComponent.builder().text(" Try to ").color(ChatColor.GRAY).build())
				.component(FancyComponent.builder().text("hover").color(ChatColor.WHITE)
						.style(ChatColor.ITALIC, ChatColor.UNDERLINE)
						.tooltip(ChatColor.LIGHT_PURPLE + "Hover on the commands to get info about them.").build())
				.component(FancyComponent.builder().text(" or ").color(ChatColor.GRAY).build())
				.component(FancyComponent.builder().text("click").color(ChatColor.WHITE)
						.style(ChatColor.ITALIC, ChatColor.UNDERLINE)
						.tooltip(ChatColor.LIGHT_PURPLE + "Click on the commands to insert them in the chat.").build())
				.component(FancyComponent.builder().text(" on the commands!").color(ChatColor.GRAY).build())
				.build();
		HolographicDisplays.getNMSManager().sendFancyMessage(message, (Player) sender);
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
