/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.commands.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.StringUtil;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import com.gmail.filoghost.holographicdisplays.commands.main.subs.DebugCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.DeleteCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.EditCommand;
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
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class HologramsCommandHandler implements CommandExecutor, TabCompleter {
	private final List<String> completions = new ArrayList<String>();
	private final List<String> partialCompletions = new ArrayList<String>();

	private List<HologramSubCommand> subCommands;
	private Map<Class<? extends HologramSubCommand>, HologramSubCommand> subCommandsByClass;

	public HologramsCommandHandler() {
		subCommands = new ArrayList<>();
		subCommandsByClass = new HashMap<>();

		registerSubCommand(new AddlineCommand());
		registerSubCommand(new CreateCommand());
		registerSubCommand(new DeleteCommand());
		registerSubCommand(new EditCommand());
		registerSubCommand(new ListCommand());
		registerSubCommand(new NearCommand());
		registerSubCommand(new TeleportCommand());
		registerSubCommand(new MovehereCommand());
		registerSubCommand(new AlignCommand());
		registerSubCommand(new CopyCommand());
		registerSubCommand(new ReloadCommand());

		registerSubCommand(new RemovelineCommand());
		registerSubCommand(new SetlineCommand());
		registerSubCommand(new InsertlineCommand());
		registerSubCommand(new ReadtextCommand());
		registerSubCommand(new ReadimageCommand());
		registerSubCommand(new InfoCommand());

		registerSubCommand(new DebugCommand());
		registerSubCommand(new HelpCommand());
	}

	public void registerSubCommand(HologramSubCommand subCommand) {
		subCommands.add(subCommand);
		subCommandsByClass.put(subCommand.getClass(), subCommand);
	}

	public List<HologramSubCommand> getSubCommands() {
		return new ArrayList<>(subCommands);
	}

	public HologramSubCommand getSubCommand(Class<? extends HologramSubCommand> subCommandClass) {
		return subCommandsByClass.get(subCommandClass);
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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		completions.clear();
		partialCompletions.clear();

		if (sender.hasPermission("holograms.help")) {
			if (args.length == 1) {
				completions.add("help");
				completions.add("delete");
				completions.add("create");
				completions.add("edit");
				completions.add("list");
				completions.add("near");
				completions.add("teleport");
				completions.add("movehere");
				completions.add("align");
				completions.add("copy");
				completions.add("reload");
				//edit commands
				completions.add("addline");
				completions.add("removeline");
				completions.add("setline");
				completions.add("insertline");
				completions.add("readtext");
				completions.add("readimage");
				completions.add("info");

			} else if (args.length == 2) {
				if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("movehere") || args[0].equalsIgnoreCase("copy") || args[0].equalsIgnoreCase("addline") || args[0].equalsIgnoreCase("removeline") || args[0].equalsIgnoreCase("setline") || args[0].equalsIgnoreCase("insertline") || args[0].equalsIgnoreCase("readline") || args[0].equalsIgnoreCase("readimage") || args[0].equalsIgnoreCase("info")){
					for(final NamedHologram namedHologram : NamedHologramManager.getHolograms()){
						completions.add(namedHologram.getName());
					}
				}else if(args[0].equalsIgnoreCase("create")){
					completions.add("<Enter new hologram name>");
					return completions;
				}

			} else if (args.length >= 3 && args[0].equalsIgnoreCase("create")) {
				StringBuilder text = new StringBuilder();
				for(int i=2; i<args.length; i++){
					if(i==args.length-1){
						text.append(args[i]);
					}else{
						text.append(args[i]).append(" ");
					}

				}
				if(!addHologramTextCompletion(text.toString())){
					return completions;
				}

			} else if (args.length >= 3 && args[0].equalsIgnoreCase("addline")) {
				StringBuilder text = new StringBuilder();
				for(int i=2; i<args.length; i++){
					if(i==args.length-1){
						text.append(args[i]);
					}else{
						text.append(args[i]).append(" ");
					}

				}
				if(!addHologramTextCompletion(text.toString())){
					return completions;
				}

			} else if (args.length == 3){
				if(args[0].equalsIgnoreCase("removeline")){
					try {
						NamedHologram hologram = CommandValidator.getNamedHologram(args[1]);
						for(int line=1; line <= hologram.size(); line++){
							completions.add(""+line);
						}
					} catch (CommandException e) {
						completions.add("<line number>");
					}

				}else if(args[0].equalsIgnoreCase("setline")){
					try {
						NamedHologram hologram = CommandValidator.getNamedHologram(args[1]);
						for(int line=1; line <= hologram.size(); line++){
							completions.add(""+line);
						}
					} catch (CommandException e) {
						completions.add("<line number>");
					}

				}else if(args[0].equalsIgnoreCase("insertline")){
					try {
						NamedHologram hologram = CommandValidator.getNamedHologram(args[1]);
						for(int line=1; line <= hologram.size(); line++){
							completions.add(""+line);
						}
					} catch (CommandException e) {
						completions.add("<line number>");
					}
				}

			} else if (args.length >= 4 && args[0].equalsIgnoreCase("setline")) {
				StringBuilder text = new StringBuilder();
				for(int i=3; i<args.length; i++){
					if(i==args.length-1){
						text.append(args[i]);
					}else{
						text.append(args[i]).append(" ");
					}

				}
				if(!addHologramTextCompletion(text.toString())){
					return completions;
				}

			} else if (args.length >= 4 && args[0].equalsIgnoreCase("insertline")) {
				StringBuilder text = new StringBuilder();
				for(int i=3; i<args.length; i++){
					if(i==args.length-1){
						text.append(args[i]);
					}else{
						text.append(args[i]).append(" ");
					}

				}
				if(!addHologramTextCompletion(text.toString())){
					return completions;
				}

			}
		}

		StringUtil.copyPartialMatches(args[args.length - 1], completions,partialCompletions);
		return partialCompletions;
	}


	public final boolean addHologramTextCompletion(final String hologramText){
		ArrayList<String> splitText = new ArrayList<>(Arrays.asList(hologramText.split(" ")));

		if(hologramText.endsWith(" ")){
			splitText.add(" ");
		}


		int argsLength = splitText.size();
		if(hologramText.isEmpty()){
			argsLength = 0;
		}


		if(argsLength > 1 && splitText.get(0).contains("ICON:")){
			for (final Material material : Material.values()) {
				completions.add(material.toString());
			}
		}else{
			if(argsLength == 0){
				completions.add("<Enter text>");
				completions.add("ICON:");
			}else{
				completions.add("<Enter text>");
				return false;
			}

		}
		return true;
	}
}
