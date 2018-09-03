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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class AddlineCommand extends HologramSubCommand {

	public AddlineCommand() {
		super("addline");
		setPermission(Strings.BASE_PERM + "addline");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> <text>";
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[0].toLowerCase()));
		String line = Utils.join(args, " ", 1, args.length);
		
		// Check material validity
		if (line.toLowerCase().startsWith("icon:")) {
			String iconMaterial = ItemUtils.stripSpacingChars(line.substring("icon:".length(), line.length()));
			
			if (iconMaterial.contains(":")) {
				iconMaterial = iconMaterial.split(":")[0];
			}
			
			Material mat = ItemUtils.matchMaterial(iconMaterial);
			CommandValidator.notNull(mat, "Invalid icon material.");
		}

		hologram.getLinesUnsafe().add(HologramDatabase.readLineFromString(line, hologram));
		hologram.refreshAll();
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Colors.PRIMARY + "Line added!");
		Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Adds a line to an existing hologram.");
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.EDIT_LINES;
	}

}
