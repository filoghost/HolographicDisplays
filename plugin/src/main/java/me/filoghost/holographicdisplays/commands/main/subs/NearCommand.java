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

import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearCommand extends HologramSubCommand {

	public NearCommand() {
		super("near");
		setPermission(Strings.BASE_PERM + "near");
	}

	@Override
	public String getPossibleArguments() {
		return "<radius>";
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		Player player = CommandValidator.getPlayerSender(sender);
		int radius = CommandValidator.getInteger(args[0]);
		CommandValidator.isTrue(radius > 0, "Radius must be at least 1.");
		
		World world = player.getWorld();
		int radiusSquared = radius * radius;
		List<NamedHologram> nearHolograms = new ArrayList<>();
		
		for (NamedHologram hologram : NamedHologramManager.getHolograms()) {
			if (hologram.getLocation().getWorld().equals(world) && hologram.getLocation().distanceSquared(player.getLocation()) <= radiusSquared) {
				nearHolograms.add(hologram);
			}
		}
		
		CommandValidator.isTrue(!nearHolograms.isEmpty(), "There are no holograms in the given radius.");
		
		player.sendMessage(Strings.formatTitle("Near holograms"));
		for (NamedHologram nearHologram : nearHolograms) {
			player.sendMessage(Colors.SECONDARY_SHADOW + "- " + Colors.SECONDARY + Colors.BOLD + nearHologram.getName() + " " + Colors.SECONDARY_SHADOW + "at x: " + (int) nearHologram.getX() + ", y: " + (int) nearHologram.getY() + ", z: " + (int) nearHologram.getZ() + " (lines: " + nearHologram.size() + ")");
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Get a list of near holograms.");
	}

	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
