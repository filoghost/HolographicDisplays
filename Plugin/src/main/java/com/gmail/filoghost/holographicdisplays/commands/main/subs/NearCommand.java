package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.util.Utils;

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
		List<NamedHologram> nearHolograms = Utils.newList();
		
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
