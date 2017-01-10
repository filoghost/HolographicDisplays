package com.gmail.filoghost.holographicdisplays.placeholder;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlayerRelativePlaceholderReplacer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.VersionUtils;

public class PlaceholdersRegister {
	
	private static final Set<Placeholder> placeholders = Utils.newSet();

	private static final Map<String, PlayerRelativePlaceholderReplacer> playerRelativeReplacers = Utils.newMap();
	private static boolean isUpdateScheduled = false, hasUndergoneFirstUpdate = false;

	// Register the default placeholders statically.
	static {
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{online}", 1.0, new PlaceholderReplacer() {
			
			@Override
			public String update() {
				return String.valueOf(VersionUtils.getOnlinePlayers().size());
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{max_players}", 10.0, new PlaceholderReplacer() {

			@Override
			public String update() {
				return String.valueOf(Bukkit.getMaxPlayers());
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{motd}", 60.0, new PlaceholderReplacer() {

			@Override
			public String update() {
				return Bukkit.getMotd();
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{time}", 0.9, new PlaceholderReplacer() {

			@Override
			public String update() {
				return Configuration.timeFormat.format(new Date());
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "&u", 0.2, new CyclicPlaceholderReplacer(Utils.arrayToStrings(
				ChatColor.RED,
				ChatColor.GOLD,
				ChatColor.YELLOW,
				ChatColor.GREEN,
				ChatColor.AQUA,
				ChatColor.LIGHT_PURPLE
		))));

		register(new Placeholder(HolographicDisplays.getInstance(), "{player}", 60.0, new PlayerRelativePlaceholderReplacer() {

			@Override
			public String update(Player player) {
				return player.getName();
			}
		}));

		register(new Placeholder(HolographicDisplays.getInstance(), "{displayname}", 60.0, new PlayerRelativePlaceholderReplacer() {

			@Override
			public String update(Player player) {
				return player.getDisplayName();
			}
		}));

		register(new Placeholder(HolographicDisplays.getInstance(), "{gamemode}", 60.0, new PlayerRelativePlaceholderReplacer() {
			@Override
			public String update(Player player) {
				return player.getGameMode().toString();
			}
		}));
	}

	public static void updatePlayerRelativeReplacers() {
		playerRelativeReplacers.clear();

		for (Placeholder placeholder : placeholders) {
			if (placeholder.isPlayerRelative()) {
				playerRelativeReplacers.put(placeholder.getTextPlaceholder(), placeholder.getPlayerRelativeReplacer());
			}
		}

		hasUndergoneFirstUpdate = true;
	}
	
	public static boolean register(Placeholder placeholder) {
		if (placeholders.contains(placeholder)) {
			return false;
		}

		if (placeholder.isPlayerRelative()) {
			schedulePlayerRelativePlaceholdersUpdate();
		}
		
		placeholders.add(placeholder);
		return true;
	}
	
	public static Set<String> getTextPlaceholdersByPlugin(Plugin plugin) {
		Set<String> found = Utils.newSet();
		
		for (Placeholder placeholder : placeholders) {
			if (placeholder.getOwner().equals(plugin)) {
				found.add(placeholder.getTextPlaceholder());
			}
		}
		
		return found;
	}
	
	public static boolean unregister(Plugin plugin, String textPlaceholder) {
		
		Iterator<Placeholder> iter = placeholders.iterator();
		
		while (iter.hasNext()) {
			Placeholder placeholder = iter.next();
			
			if (placeholder.getOwner().equals(plugin) && placeholder.getTextPlaceholder().equals(textPlaceholder)) {
				iter.remove();
			
				for (DynamicLineData data : PlaceholdersManager.linesToUpdate) {
					if (data.getPlaceholders().contains(placeholder)) {
						data.getPlaceholders().remove(placeholder);
					}
				}

				if (placeholder.isPlayerRelative()) {
					schedulePlayerRelativePlaceholdersUpdate();
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	protected static Set<Placeholder> getPlaceholders() {
		return placeholders;
	}

	protected static Map<String, PlayerRelativePlaceholderReplacer> getPlayerRelativeReplacers() {
		return playerRelativeReplacers;
	}

	private static void schedulePlayerRelativePlaceholdersUpdate() {
		if (!isUpdateScheduled && hasUndergoneFirstUpdate) {
			isUpdateScheduled = true;

			Bukkit.getScheduler().scheduleSyncDelayedTask(HolographicDisplays.getInstance(), new Runnable() {

				@Override
				public void run() {
					updatePlayerRelativeReplacers();
				}
			});
		}
	}
}
