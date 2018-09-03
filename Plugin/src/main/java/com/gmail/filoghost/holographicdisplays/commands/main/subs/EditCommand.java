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
package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramsCommandHandler;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.util.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EditCommand extends HologramSubCommand {
	
	private HologramsCommandHandler mainCommandHandler;

	public EditCommand(HologramsCommandHandler mainCommandHandler) {
		super("edit");
		setPermission(Strings.BASE_PERM + "edit");
		this.mainCommandHandler = mainCommandHandler;
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
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		String name = args[0].toLowerCase();
		NamedHologram hologram = NamedHologramManager.getHologram(name);
		CommandValidator.notNull(hologram, Strings.noSuchHologram(name));
		
		sender.sendMessage("");
		sender.sendMessage(Strings.formatTitle("How to edit the hologram '" + name + "'"));
		for (HologramSubCommand subCommand : mainCommandHandler.getSubCommands()) {
			if (subCommand.getType() == SubCommandType.EDIT_LINES) {
				String usage = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments().replace("<hologramName>", hologram.getName()).replace("<hologram>", hologram.getName()) : "");
				
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
			HelpCommand.sendHoverTip((Player) sender);
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
