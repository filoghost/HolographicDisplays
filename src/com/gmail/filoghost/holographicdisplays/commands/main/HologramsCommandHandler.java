package com.gmail.filoghost.holographicdisplays.commands.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.AddlineCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.AlignCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.CopyCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.CreateCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.DeleteCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.EditCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.FixCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.HelpCommand;
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
		registerSubCommand(new FixCommand());
		
		registerSubCommand(new RemovelineCommand());
		registerSubCommand(new SetlineCommand());
		registerSubCommand(new InsertlineCommand());
		registerSubCommand(new ReadtextCommand());
		registerSubCommand(new ReadimageCommand());
		registerSubCommand(new InfoCommand());
		
		registerSubCommand(new HelpCommand(this));
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
			sender.sendMessage(Colors.PRIMARY_SHADOW + "Server is running " + Colors.PRIMARY + "Holographic Displays " + Colors.PRIMARY_SHADOW + "v" + HolographicDisplays.getInstance().getDescription().getVersion() + " by " + Colors.PRIMARY + "filoghost");
			if (sender.hasPermission(Strings.BASE_PERM + "help")) {
				sender.sendMessage(Colors.PRIMARY_SHADOW + "Commands: " + Colors.PRIMARY + "/" + label + " help");
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
}
