package com.gmail.filoghost.holographicdisplays.commands.main.subs;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import com.gmail.filoghost.holographicdisplays.commands.Colors;
import com.gmail.filoghost.holographicdisplays.commands.Strings;
import com.gmail.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.disk.UnicodeSymbols;
import com.gmail.filoghost.holographicdisplays.event.HolographicDisplaysReloadEvent;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicdisplays.exception.InvalidFormatException;
import com.gmail.filoghost.holographicdisplays.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.gmail.filoghost.holographicdisplays.placeholder.AnimationsRegister;
import com.gmail.filoghost.holographicdisplays.placeholder.PlaceholdersManager;

public class ReloadCommand extends HologramSubCommand {

	public ReloadCommand() {
		super("reload");
		setPermission(Strings.BASE_PERM + "reload");
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
	public void execute(CommandSender sender, String label, String[] args) throws CommandException {
		try {
			
			long startMillis = System.currentTimeMillis();

			UnicodeSymbols.load(HolographicDisplays.getInstance());
			Configuration.load(HolographicDisplays.getInstance());
			
			BungeeServerTracker.resetTrackedServers();
			BungeeServerTracker.startTask(Configuration.bungeeRefreshSeconds);
			
			HologramDatabase.loadYamlFile(HolographicDisplays.getInstance());
			AnimationsRegister.loadAnimations(HolographicDisplays.getInstance());
			
			PlaceholdersManager.untrackAll();
			NamedHologramManager.clearAll();
			
			Set<String> savedHolograms = HologramDatabase.getHolograms();
			if (savedHolograms != null && savedHolograms.size() > 0) {
				for (String singleSavedHologram : savedHolograms) {
					try {
						NamedHologram singleHologramEntity = HologramDatabase.loadHologram(singleSavedHologram);
						NamedHologramManager.addHologram(singleHologramEntity);
					} catch (HologramNotFoundException e) {
						Strings.sendWarning(sender, "Hologram '" + singleSavedHologram + "' not found, skipping it.");
					} catch (InvalidFormatException e) {
						Strings.sendWarning(sender, "Hologram '" + singleSavedHologram + "' has an invalid location format.");
					} catch (WorldNotFoundException e) {
						Strings.sendWarning(sender, "Hologram '" + singleSavedHologram + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
					}
				}
			}
			
			for (CraftHologram hologram : NamedHologramManager.getHolograms()) {
				hologram.refreshAll();
			}
			
			long endMillis = System.currentTimeMillis();
			
			sender.sendMessage(Colors.PRIMARY + "Configuration reloaded successfully in " + (endMillis - startMillis) + "ms!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CommandException("Exception while reloading the configuration. Please look the console.");
		}
		
		Bukkit.getPluginManager().callEvent(new HolographicDisplaysReloadEvent());
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
