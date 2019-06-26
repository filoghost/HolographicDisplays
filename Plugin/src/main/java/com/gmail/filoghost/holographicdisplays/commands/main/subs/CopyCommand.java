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
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;

public class CopyCommand extends HologramSubCommand {
	
	public CopyCommand() {
		super("copy");
		setPermission(Strings.BASE_PERM + "copy");
	}

	@Override
	public String getPossibleArguments() {
		return "<fromHologram> <toHologram>";
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		NamedHologram fromHologram = CommandValidator.getNamedHologram(args[0]);
		NamedHologram toHologram = CommandValidator.getNamedHologram(args[1]);
		
		toHologram.clearLines();
		for (CraftHologramLine line : fromHologram.getLinesUnsafe()) {
			String lineString = HologramDatabase.saveLineToString(line);
			toHologram.getLinesUnsafe().add(HologramDatabase.readLineFromString(lineString, toHologram));
		}
		
		toHologram.refreshAll();
		
		HologramDatabase.saveHologram(toHologram);
		HologramDatabase.trySaveToDisk();
		
		sender.sendMessage(Colors.PRIMARY + "Hologram \"" + fromHologram.getName() + "\" copied into hologram \"" + toHologram.getName() + "\"!");
	}
	
	@Override
	public List<String> getTutorial() {
		return Arrays.asList(
				"Copies the contents of a hologram into another one.");
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
