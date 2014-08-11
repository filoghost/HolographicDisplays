package com.gmail.filoghost.holograms.commands.main.subs;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holograms.Configuration;
import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.bungee.ServerInfoTimer;
import com.gmail.filoghost.holograms.commands.Messages;
import com.gmail.filoghost.holograms.commands.main.HologramSubCommand;
import com.gmail.filoghost.holograms.database.HologramDatabase;
import com.gmail.filoghost.holograms.exception.CommandException;
import com.gmail.filoghost.holograms.exception.HologramNotFoundException;
import com.gmail.filoghost.holograms.exception.InvalidLocationException;
import com.gmail.filoghost.holograms.exception.WorldNotFoundException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.object.HologramManager;
import com.gmail.filoghost.holograms.placeholders.AnimationManager;
import com.gmail.filoghost.holograms.placeholders.StaticPlaceholders;
import com.gmail.filoghost.holograms.utils.Format;

public class ReloadCommand extends HologramSubCommand {

	public ReloadCommand() {
		super("reload");
		setPermission(Messages.BASE_PERM + "reload");
	}

	@Override
	public String getPossibleArguments() {
		return "";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		try {
			
			long startMillis = System.currentTimeMillis();
			
			HolographicDisplays.getInstance().reloadConfig();
			HolographicDisplays.getInstance().loadConfiguration();
			
			ServerInfoTimer.setRefreshSeconds(Configuration.bungeeRefreshSeconds);
			ServerInfoTimer.startTask();
			
			StaticPlaceholders.load();
			
			HologramDatabase.initialize();
			HologramManager.clearAll();
			AnimationManager.loadAnimations();

			Set<String> savedHolograms = HologramDatabase.getHolograms();
			if (savedHolograms != null && savedHolograms.size() > 0) {
				for (String singleSavedHologram : savedHolograms) {
					try {
						CraftHologram singleHologramEntity = HologramDatabase.loadHologram(singleSavedHologram);
						HologramManager.addHologram(singleHologramEntity);
					} catch (HologramNotFoundException e) {
						Format.sendWarning(sender, "Hologram '" + singleSavedHologram + "' not found, skipping it.");
					} catch (InvalidLocationException e) {
						Format.sendWarning(sender, "Hologram '" + singleSavedHologram + "' has an invalid location format.");
					} catch (WorldNotFoundException e) {
						Format.sendWarning(sender, "Hologram '" + singleSavedHologram + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
					}
				}
			}
			
			for (CraftHologram hologram : HologramManager.getHolograms()) {
				if (!hologram.update()) {
					sender.sendMessage("§c[ ! ] §7Unable to spawn entities for the hologram '" + hologram.getName() + "'.");
				}
			}
			
			long endMillis = System.currentTimeMillis();
			
			sender.sendMessage("§bConfiguration reloaded successfully in " + (endMillis - startMillis) + "ms!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CommandException("Exception while reloading the configuration. Please look the console.");
		}
	}
	
	@Override
	public List<String> getTutorial() {
		return Arrays.asList("Reloads the holograms from the database.");
	}
	
	@Override
	public SubCommandType getType() {
		return SubCommandType.GENERIC;
	}

}
