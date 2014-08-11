package com.gmail.filoghost.holograms.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.database.HologramDatabase;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.exception.InvalidCharactersException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.Format;
import com.gmail.filoghost.holograms.utils.StringUtils;

public class CreateCommand extends HologramSubCommand {


	public CreateCommand() {
		super("create");
		setPermission(Messages.BASE_PERM + "create");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> [text]";
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}


	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		try {
			Player player = CommandValidator.getPlayerSender(sender);
			String name = StringUtils.validateName(args[0].toLowerCase());
			CommandValidator.isTrue(!HologramManager.isExistingHologram(name), "A hologram with that name already exists.");
			
			Location spawnLoc = player.getLocation();
			boolean moveUp = player.isOnGround();
			
			if (moveUp) {
				spawnLoc.add(0.0, 1.2, 0.0);
			}
			
			CraftHologram hologram = new CraftHologram(name, spawnLoc);
			HologramManager.addHologram(hologram);
			
			if (args.length > 1) {
				hologram.addLine(StringUtils.toReadableFormat(StringUtils.join(args, " ", 1, args.length)));
				player.sendMessage("§7(Change the lines with /hd edit " + hologram.getName() + ")");
			} else {
				hologram.addLine("Default hologram. Change it with " + Format.HIGHLIGHT + "/hd edit " + hologram.getName());
			}
				
			if (!hologram.forceUpdate()) {
				player.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
			}
			
			HologramDatabase.saveHologram(hologram);
			HologramDatabase.trySaveToDisk();
			Location look = player.getLocation();
			look.setPitch(90);
			player.teleport(look, TeleportCause.PLUGIN);
			player.sendMessage(Format.HIGHLIGHT + "You created a hologram named '" + hologram.getName() + "'.");
			
			if (moveUp) {
				player.sendMessage("§7(You were on the ground, the hologram was automatically moved up. If you use /hd movehere " + hologram.getName() + ", the hologram will be moved to your feet)");
			}
			
		} catch (InvalidCharactersException ex) {
			throw new CommandException("The hologram's name must be alphanumeric. '" + ex.getMessage() + "' is not allowed.");
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Creates a new hologram with the given name, that must",
							 "be alphanumeric. The name will be used as reference to",
							 "that hologram for editing commands.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
