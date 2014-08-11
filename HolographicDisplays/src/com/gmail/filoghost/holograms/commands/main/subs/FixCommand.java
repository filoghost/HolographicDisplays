package com.gmail.filoghost.holograms.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import com.gmail.filoghost.holograms.commands.CommandValidator;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.utils.Format;

public class FixCommand extends HologramSubCommand {

	public FixCommand() {
		super("fix", "light");
		setPermission(Messages.BASE_PERM + "fix");
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
	public void execute(CommandSender sender, String[] args) throws CommandException {
		CraftHologram hologram = HologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);

		if (args.length <= 1) {	
			sender.sendMessage(Format.HIGHLIGHT + "This command will put a glowstone 16 blocks above the hologram to fix the lightning.");
			sender.sendMessage(Format.HIGHLIGHT + "If you're sure, type §f/hd fix " + args[0].toLowerCase() + " confirm");
			return;
		}
		
		if (args[1].equalsIgnoreCase("confirm")) {
			
			Block block = hologram.getWorld().getBlockAt(hologram.getBlockX(), hologram.getBlockY() + 16, hologram.getBlockZ());
			String oldType = block.getType().toString().replace("_", " ").toLowerCase();
			block.setType(Material.GLOWSTONE);
			
			sender.sendMessage(Format.HIGHLIGHT + "Changed the block 16 block above the hologram (" + oldType + ") to glowstone!");

		} else {
			sender.sendMessage("§c" + args[1] + " is not a valid confirmation! Use \"confirm\".");
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("This command will fix the lightning of a hologram,",
							"placing a glowstone block 16 blocks above it.",
							"That's the only way to fix it.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
