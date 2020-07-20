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
package com.gmail.filoghost.holographicdisplays.placeholder;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import com.gmail.filoghost.holographicdisplays.task.WorldPlayerCounterTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class PlaceholdersRegister {
	
	private static final Set<Placeholder> placeholders = new HashSet<>();
	private static final Set<PatternPlaceholder> patternPlaceholders = new HashSet<>();
	
	// Register the default placeholders.
	static {
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{online}", 1.0, () -> {
			return String.valueOf(Bukkit.getOnlinePlayers().size());
		}));
		
		// BungeeCord online pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), makePlaceholderWithArgsPattern("online"), 1.0, (matcher) -> {
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			if (serverName.contains(",")) {
				final String[] split = serverName.split(",");
				for (int i = 0; i < split.length; i++) {
					split[i] = split[i].trim();
				}
				
				// Add it to tracked servers.
				int count = 0;
				for (String serverToTrack : split) {
					count += BungeeServerTracker.getPlayersOnline(serverToTrack);
				}
				return String.valueOf(count);
			} else {
				// Normal, single tracked server.
				return String.valueOf(BungeeServerTracker.getPlayersOnline(serverName));
			}
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{max_players}", 10.0, () -> {
			return String.valueOf(Bukkit.getMaxPlayers());
		}));
		
		// BungeeCord max players pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), makePlaceholderWithArgsPattern("max_players"), 10.0, (matcher) -> {
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			return BungeeServerTracker.getMaxPlayers(serverName);
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{motd}", 60.0, () -> {
			return Bukkit.getMotd();
		}));
		
		// BungeeCord motd pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), makePlaceholderWithArgsPattern("motd"), 10.0, (matcher) -> {
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			return BungeeServerTracker.getMotd1(serverName);
		}));
		
		// BungeeCord motd (line 2) pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), makePlaceholderWithArgsPattern("motd2"), 10.0, (matcher) -> {
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			return BungeeServerTracker.getMotd2(serverName);
		}));
		
		// BungeeCord status pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), makePlaceholderWithArgsPattern("status"), 10.0, (matcher) -> {
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			return BungeeServerTracker.getOnlineStatus(serverName);
		}));
		
		register(new Placeholder(HolographicDisplays.getInstance(), "{time}", 0.9, () -> {
			return Configuration.timeFormat.format(new Date());
		}));
		
		// Players in a world count pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), makePlaceholderWithArgsPattern("world"), 60.0, (matcher) -> {
			final String worldsNames = extractArgumentFromPlaceholder(matcher);
			
			if (worldsNames.contains(",")) {
				String[] worldsToTrack = worldsNames.split(",");
				for (int i = 0; i < worldsToTrack.length; i++) {
					worldsToTrack[i] = worldsToTrack[i].trim();
				}
				
				// Add it to tracked worlds.
				return WorldPlayerCounterTask.getCount(worldsToTrack);
			} else {
				// Normal, single tracked world.
				return WorldPlayerCounterTask.getCount(worldsNames);
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
	}
	
	
	private static Pattern makePlaceholderWithArgsPattern(String prefix) {
		return Pattern.compile("\\{" + Pattern.quote(prefix) + ":(.+?)}");
	}
	
	private static String extractArgumentFromPlaceholder(Matcher matcher) {
		return matcher.group(1).trim();
	}
	
	public static boolean register(Placeholder placeholder) {
		if (placeholders.contains(placeholder)) {
			return false;
		}
		
		placeholders.add(placeholder);
		return true;
	}
	
	public static boolean register(PatternPlaceholder placeholder) {
		if (patternPlaceholders.contains(placeholder)) {
			return false;
		}
		
		patternPlaceholders.add(placeholder);
		return true;
	}
	
	public static Set<String> getTextPlaceholdersByPlugin(Plugin plugin) {
		Set<String> found = new HashSet<>();
		
		for (Placeholder placeholder : placeholders) {
			if (placeholder.getOwner().equals(plugin)) {
				found.add(placeholder.getTextPlaceholder());
			}
		}
		
		return found;
	}
	
	public static Set<Pattern> getPatternPlaceholdersByPlugin(Plugin plugin) {
		Set<Pattern> found = new HashSet<>();
		
		for (PatternPlaceholder placeholder : patternPlaceholders) {
			if (placeholder.getOwner().equals(plugin)) {
				if (placeholder.getOwner().equals(plugin)) {
					found.add(placeholder.getPatternPlaceholder());
				}
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
					data.getPlaceholders().remove(placeholder);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean unregister(Plugin plugin, Pattern patternPlaceholder) {
		Iterator<PatternPlaceholder> iter = patternPlaceholders.iterator();
		
		while (iter.hasNext()) {
			PatternPlaceholder placeholder = iter.next();
			
			if (placeholder.getOwner().equals(plugin) && placeholder.getPatternPlaceholder().equals(patternPlaceholder)) {
				iter.remove();
				
				for (DynamicLineData data : PlaceholdersManager.linesToUpdate) {
					data.getPatternPlaceholders().remove(placeholder);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	
	protected static Set<Placeholder> getPlaceholders() {
		return placeholders;
	}
	
	protected static Set<PatternPlaceholder> getPatternPlaceholders() {
		return patternPlaceholders;
	}
	
}
