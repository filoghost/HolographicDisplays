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
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.exception.CommandException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends HologramSubCommand {

	public HelpCommand() {
		super("help");
		setPermission(Strings.BASE_PERM + "help");
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
		for (HologramSubCommand subCommand : HolographicDisplays.getCommandHandler().getSubCommands()) {
			if (subCommand.getType() == SubCommandType.GENERIC) {
				String usage = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments() : "");
				
				if (CommandValidator.isPlayerSender(sender)) {
					
					List<String> help = new ArrayList<>();
					help.add(Colors.PRIMARY + usage);
					for (String tutLine : subCommand.getTutorial()) {
						help.add(Colors.SECONDARY_SHADOW + tutLine);
					}
					
					((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
						.color(ChatColor.AQUA)
						.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
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
		player.spigot().sendMessage(new ComponentBuilder("TIP:").color(ChatColor.YELLOW).bold(true)
			.append(" Try to ", FormatRetention.NONE).color(ChatColor.GRAY)
			.append("hover").color(ChatColor.WHITE).underlined(true)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Hover on the commands to get info about them.")))
			.append(" or ", FormatRetention.NONE).color(ChatColor.GRAY)
			.append("click").color(ChatColor.WHITE).underlined(true)
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
