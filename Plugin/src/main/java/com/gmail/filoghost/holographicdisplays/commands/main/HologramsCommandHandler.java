package com.gmail.filoghost.holographicdisplays.commands.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand.SubCommandType;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.AddlineCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.AlignCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.CopyCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.CreateCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.DeleteCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.EditCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.FixCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.InfoCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.InsertlineCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.ListCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.MovehereCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.NearCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.ReadimageCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.ReadtextCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.ReloadCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.RemovelineCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.SetlineCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.TeleportCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.VersionCommand;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class HologramsCommandHandler implements CommandExecutor {

	private List<HologramSubCommand> subCommands;

	public HologramsCommandHandler() {
		subCommands = Utils.newList();

		registerSubCommand(new AddlineCommand());
		registerSubCommand(new CreateCommand());
		registerSubCommand(new DeleteCommand());
		registerSubCommand(new EditCommand(this));
		registerSubCommand(new ListCommand());
		registerSubCommand(new NearCommand());
		registerSubCommand(new TeleportCommand());
		registerSubCommand(new MovehereCommand());
		registerSubCommand(new AlignCommand());
		registerSubCommand(new CopyCommand());
		registerSubCommand(new ReloadCommand());
		registerSubCommand(new VersionCommand());
		registerSubCommand(new FixCommand());

		registerSubCommand(new RemovelineCommand());
		registerSubCommand(new SetlineCommand());
		registerSubCommand(new InsertlineCommand());
		registerSubCommand(new ReadtextCommand());
		registerSubCommand(new ReadimageCommand());
		registerSubCommand(new InfoCommand());
	}

	public void registerSubCommand(HologramSubCommand subCommand) {
		subCommands.add(subCommand);
	}

	public List<HologramSubCommand> getSubCommands() {
		return new ArrayList<HologramSubCommand>(subCommands);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			sender.sendMessage("");
			sender.sendMessage(Strings.formatTitle("Holographic Displays Commands"));
			for (HologramSubCommand subCommand : subCommands) {
				if (subCommand.getType() == SubCommandType.GENERIC) {
					String usage = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments() : "");

					if (CommandValidator.isPlayerSender(sender)) {

						List<String> help = Utils.newList();
						help.add(Colors.PRIMARY + usage);
						for (String tutLine : subCommand.getTutorial()) {
							help.add(Colors.SECONDARY_SHADOW + tutLine);
						}

						HolographicDisplays.getNMSManager()
								.newFancyMessage(usage)
								.color(ChatColor.AQUA)
								.suggest(usage)
								.tooltip(Utils.join(help, "\n"))
								.send((Player) sender);

					} else {
						sender.sendMessage(Colors.PRIMARY + usage);
					}
				}
			}

			if (CommandValidator.isPlayerSender(sender)) {
				sendHoverTip(sender);
			}
			return true;
		}

		for (HologramSubCommand subCommand : subCommands) {
			if (subCommand.isValidTrigger(args[0])) {

				if (!subCommand.hasPermission(sender)) {
					sender.sendMessage(Colors.ERROR + "You don't have permission.");
					return true;
				}

				if (args.length - 1 >= subCommand.getMinimumArguments()) {
					try {
						subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
					} catch (CommandException e) {
						sender.sendMessage(Colors.ERROR + e.getMessage());
					}
				} else {
					sender.sendMessage(Colors.ERROR + "Usage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
				}

				return true;
			}
		}

		sender.sendMessage(Colors.ERROR + "Unknown sub-command. Type \"/" + label + " help\" for a list of commands.");
		return true;
	}
	
	public static void sendHoverTip(CommandSender sender) {
		sender.sendMessage("");
		HolographicDisplays.getNMSManager().newFancyMessage("TIP").style(ChatColor.BOLD).color(ChatColor.YELLOW)
			.then(" Try to ").color(ChatColor.GRAY)
			.then("hover").color(ChatColor.WHITE).style(ChatColor.ITALIC, ChatColor.UNDERLINE)
			.tooltip(ChatColor.LIGHT_PURPLE + "Hover on the commands to get info about them.")
			.then(" or ").color(ChatColor.GRAY)
			.then("click").color(ChatColor.WHITE).style(ChatColor.ITALIC, ChatColor.UNDERLINE)
			.tooltip(ChatColor.LIGHT_PURPLE + "Click on the commands to insert them in the chat.")
			.then(" on the commands!").color(ChatColor.GRAY)
			.send((Player) sender);
	}

}
