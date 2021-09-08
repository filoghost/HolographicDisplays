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

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;


public class TeleportCommand extends HologramSubCommand {
	
	public TeleportCommand() {
		super("teleport", "tp");
		setPermission(Strings.BASE_PERM + "teleport");
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
		Player player = CommandValidator.getPlayerSender(sender);
		NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
		
		if (hologram == null) {
			sender.sendMessage(Colors.ERROR + "Hologram " + args[0] + "does not exist!");
			return;
		}
		Location loc = hologram.getLocation();
		loc.setPitch(90);
		player.teleport(loc, TeleportCause.PLUGIN);
		player.sendMessage(Colors.PRIMARY + "You were teleported to the hologram named '" + hologram.getName() + "'.");

	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Teleports you to the given hologram.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
