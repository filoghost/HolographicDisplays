package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.util.MinecraftVersion;

public class FixCommand extends HologramSubCommand {

	public FixCommand() {
		super("fix");
		setPermission(Strings.BASE_PERM + "fix");
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
		
		CommandValidator.isTrue(!MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8), "This command is no longer necessary in 1.8+. The holograms already use the correct ambient light.");
		
		NamedHologram hologram = NamedHologramManager.getHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Strings.noSuchHologram(args[0].toLowerCase()));

		if (args.length <= 1) {
			sender.sendMessage(Colors.PRIMARY + "This command will put a glowstone 16 blocks above the hologram to fix the lightning.");
			sender.sendMessage(Colors.PRIMARY + "If you're sure, type " + Colors.SECONDARY + "/" + label + " fix " + args[0].toLowerCase() + " confirm");
			return;
		}
		
		if (args[1].equalsIgnoreCase("confirm")) {
			
			Block block = hologram.getWorld().getBlockAt((int) hologram.getX(), (int) hologram.getY() + 16, (int) hologram.getZ());
			String oldType = block.getType().toString().replace("_", " ").toLowerCase();
			block.setType(Material.GLOWSTONE);
			
			sender.sendMessage(Colors.PRIMARY + "Changed the block 16 block above the hologram (" + oldType + ") to glowstone!");

		} else {
			throw new CommandException(args[1] + " is not a valid confirmation! Use \"confirm\".");
		}
	}

	@Override
	public List<String> getTutorial() {
		return Arrays.asList("This command will fix the lightning of a hologram,",
							"placing a glowstone block 16 blocks above it.",
							"That's the only way to fix it (Only for 1.7 and lower).");
	}
	
	@Override
	public SubCommandType getType() {
		if (MinecraftVersion.isGreaterEqualThan(MinecraftVersion.v1_8)) {
			return SubCommandType.HIDDEN;
		} else {
			return SubCommandType.GENERIC;
		}
	}

}
