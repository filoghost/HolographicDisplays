package com.gmail.filoghost.holograms.commands.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.commands.main.subs.*;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.utils.Format;

public class HologramsCommandHandler implements CommandExecutor {

	private List<HologramSubCommand> subCommands;
	
	public HologramsCommandHandler() {
		subCommands = new ArrayList<HologramSubCommand>();
		
		registerSubCommand(new AddlineCommand());
		registerSubCommand(new CreateCommand());
		registerSubCommand(new DeleteCommand());
		registerSubCommand(new EditCommand());
		registerSubCommand(new ListCommand());
		registerSubCommand(new NearCommand());
		registerSubCommand(new TeleportCommand());
		registerSubCommand(new MovehereCommand());
		registerSubCommand(new FixCommand());
		registerSubCommand(new SaveCommand());
		registerSubCommand(new ReloadCommand());
		
		registerSubCommand(new RemovelineCommand());
		registerSubCommand(new SetlineCommand());
		registerSubCommand(new InsertlineCommand());
		registerSubCommand(new ReadtextCommand());
		registerSubCommand(new ReadimageCommand());
		
		registerSubCommand(new HelpCommand());
	}
	
	public void registerSubCommand(HologramSubCommand subCommand) {
		subCommands.add(subCommand);
	}
	
	public List<HologramSubCommand> getSubCommands() {
		return new ArrayList<HologramSubCommand>(subCommands);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			sender.sendMessage("");
			sender.sendMessage(Format.formatTitle("Holographic Displays"));
			sender.sendMessage(Format.HIGHLIGHT + "Version: §7" + HolographicDisplays.getInstance().getDescription().getVersion());
			sender.sendMessage(Format.HIGHLIGHT + "Developer: §7filoghost");
			sender.sendMessage(Format.HIGHLIGHT + "Commands: §7/hd help");
			return true;
		}		
		
		for (HologramSubCommand subCommand : subCommands) {
			if (subCommand.isValidTrigger(args[0])) {
				
				if (!subCommand.hasPermission(sender)) {
					sender.sendMessage("§cYou don't have permission.");
					return true;
				}
				
				if (args.length - 1 >= subCommand.getMinimumArguments()) {
					try {
						subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
					} catch (CommandException e) {
						sender.sendMessage(RED + e.getMessage());
					}
				} else {
					sender.sendMessage("§cUsage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
				}
				
				return true;
			}
		}
		
		sender.sendMessage("§cUnknown sub-command. Type \"/hd help\" for a list of commands.");
		return true;
	}
}
