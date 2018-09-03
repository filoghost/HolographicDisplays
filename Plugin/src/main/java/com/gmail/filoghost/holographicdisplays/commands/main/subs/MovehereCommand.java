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
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;

public class MovehereCommand extends HologramSubCommand {


	public MovehereCommand() {
		super("movehere");
		setPermission(Strings.BASE_PERM + "movehere");
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
		NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[0].toLowerCase()));
		
		hologram.teleport(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		hologram.despawnEntities();
		hologram.refreshAll();
		
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		Location to = player.getLocation();
		to.setPitch(90);
		player.teleport(to, TeleportCause.PLUGIN);
		player.sendMessage(Colors.PRIMARY + "You moved the hologram '" + hologram.getName() + "' near to you.");
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Moves a hologram to your location.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
