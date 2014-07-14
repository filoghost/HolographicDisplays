package com.gmail.filoghost.holograms.commands.main.subs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.Format;

public class NearCommand extends HologramSubCommand {

	public NearCommand() {
		super("near");
		setPermission(Messages.MAIN_PERMISSION);
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
	public void execute(CommandSender sender, String[] args) throws CommandException {
		Player player = CommandValidator.getPlayerSender(sender);
		int radius = CommandValidator.getInteger(args[0]);
		CommandValidator.isTrue(radius > 0, "Radius must be at least 1.");
		
		World world = player.getWorld();
		int radiusSquared = radius * radius;
		List<CraftHologram> nearHolograms = new ArrayList<CraftHologram>();
		
		for (CraftHologram hologram : HologramManager.getHolograms()) {
			if (hologram.getLocation().getWorld().equals(world) && hologram.getLocation().distanceSquared(player.getLocation()) <= radiusSquared) {
				nearHolograms.add(hologram);
			}
		}
		
		if (nearHolograms.size() > 0) {
			player.sendMessage(Format.formatTitle("Near holograms"));
			for (CraftHologram nearHologram : nearHolograms) {
				player.sendMessage("§3- §f'" + nearHologram.getName() + "' §7at x: " + nearHologram.getBlockX() + ", y: " + nearHologram.getBlockY() + ", z: " + nearHologram.getBlockZ() + " (lines: " + nearHologram.getLinesLength() + ")");
			}
		} else {
			player.sendMessage("§cThere are no holograms in the given radius.");
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
