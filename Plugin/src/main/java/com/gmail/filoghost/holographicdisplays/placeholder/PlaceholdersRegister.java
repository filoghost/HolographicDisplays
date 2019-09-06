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

    private static final Pattern BUNGEE_ONLINE_PATTERN = makePlaceholderWithArgsPattern("online");
    private static final Pattern BUNGEE_MAX_PATTERN = makePlaceholderWithArgsPattern("max_players");
    private static final Pattern BUNGEE_MOTD_PATTERN = makePlaceholderWithArgsPattern("motd");
    private static final Pattern BUNGEE_MOTD_2_PATTERN = makePlaceholderWithArgsPattern("motd2");
    private static final Pattern BUNGEE_STATUS_PATTERN = makePlaceholderWithArgsPattern("status");
    private static final Pattern WORLD_PATTERN = makePlaceholderWithArgsPattern("world");

    private static Pattern makePlaceholderWithArgsPattern(String prefix) {
        return Pattern.compile("\\{" + Pattern.quote(prefix) + ":(.+?)}");
    }

    private static String extractArgumentFromPlaceholder(Matcher matcher) {
        return matcher.group(1).trim();
    }

    // Register the default placeholders statically.
    static {

        register(new Placeholder(HolographicDisplays.getInstance(), "{online}", 1.0, () ->
                String.valueOf(Bukkit.getOnlinePlayers().size())));

        register(new Placeholder(HolographicDisplays.getInstance(), "{max_players}", 10.0, () ->
                String.valueOf(Bukkit.getMaxPlayers())));

        register(new Placeholder(HolographicDisplays.getInstance(),"{motd}", 60.0, Bukkit::getMotd));

        register(new Placeholder(HolographicDisplays.getInstance(),"{time}", 0.9, () ->
                Configuration.timeFormat.format(new Date())));

        register(new Placeholder(HolographicDisplays.getInstance(), "&u", 0.2, new CyclicPlaceholderReplacer(Utils.arrayToStrings(
                ChatColor.RED,
                ChatColor.GOLD,
                ChatColor.YELLOW,
                ChatColor.GREEN,
                ChatColor.AQUA,
                ChatColor.LIGHT_PURPLE
        ))));

        // Players in a world count pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), WORLD_PATTERN, 60.0, (matcher) -> {
            final String worldName = extractArgumentFromPlaceholder(matcher);
            return WorldPlayerCounterTask.getCount(worldName);
        }));

        // BungeeCord online pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), BUNGEE_ONLINE_PATTERN, 1.0, (matcher) -> {
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

        // BungeeCord max players pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), BUNGEE_MAX_PATTERN, 10.0, (matcher) -> {
            final String serverName = extractArgumentFromPlaceholder(matcher);
            BungeeServerTracker.track(serverName); // Track this server.

            return BungeeServerTracker.getMaxPlayers(serverName);
        }));

        // BungeeCord motd pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), BUNGEE_MOTD_PATTERN, 10.0, (matcher) -> {
            final String serverName = extractArgumentFromPlaceholder(matcher);
            BungeeServerTracker.track(serverName); // Track this server.

            return BungeeServerTracker.getMotd1(serverName);
        }));

        // BungeeCord motd (line 2) pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), BUNGEE_MOTD_2_PATTERN, 10.0, (matcher) -> {
            final String serverName = extractArgumentFromPlaceholder(matcher);
            BungeeServerTracker.track(serverName); // Track this server.

            return BungeeServerTracker.getMotd2(serverName);
        }));

        // BungeeCord status pattern.
		register(new PatternPlaceholder(HolographicDisplays.getInstance(), BUNGEE_STATUS_PATTERN, 10.0, (matcher) -> {
            final String serverName = extractArgumentFromPlaceholder(matcher);
            BungeeServerTracker.track(serverName); // Track this server.

            return BungeeServerTracker.getOnlineStatus(serverName);
        }));
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
				found.add(placeholder.getPatternPlaceholder());
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
					data.getPlaceholders().remove(placeholder);
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
