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

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;

public class ListCommand extends HologramSubCommand {

	private static final int HOLOGRAMS_PER_PAGE = 10;

	public ListCommand() {
		super("list");
		setPermission(Strings.BASE_PERM + "list");
	}

	@Override
	public String getPossibleArguments() {
		return "[page]";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {

		int page = args.length > 0 ? CommandValidator.getInteger(args[0]) : 1;

		if (page < 1) {
			throw new CommandException("Page number must be 1 or greater.");
		}

		int totalPages = NamedHologramManager.size() / HOLOGRAMS_PER_PAGE;
		if (NamedHologramManager.size() % HOLOGRAMS_PER_PAGE != 0) {
			totalPages++;
		}
		
		
		if (NamedHologramManager.size() == 0) {
			throw new CommandException("There are no holograms yet. Create one with /" + label + " create.");
		}

		sender.sendMessage("");
		sender.sendMessage(Strings.formatTitle("Holograms list " + Colors.SECONDARY + "(Page " + page + " of " + totalPages + ")"));
		int fromIndex = (page - 1) * HOLOGRAMS_PER_PAGE;
		int toIndex = fromIndex + HOLOGRAMS_PER_PAGE;

		for (int i = fromIndex; i < toIndex; i++) {
			if (i < NamedHologramManager.size()) {
				NamedHologram hologram = NamedHologramManager.get(i);
				sender.sendMessage(Colors.SECONDARY_SHADOW + "- " + Colors.SECONDARY + Colors.BOLD + hologram.getName() + " " + Colors.SECONDARY_SHADOW + "at x: " + (int) hologram.getX() + ", y: " + (int) hologram.getY() + ", z: " + (int) hologram.getZ() + " (lines: " + hologram.size() + ", world: \"" + hologram.getWorld().getName() + "\")");
			}
		}
		if (page < totalPages) {
			sender.sendMessage(Strings.TIP_PREFIX + "See the next page with /" + label + " list " + (page + 1));
		}

	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Lists all the existing holograms.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
