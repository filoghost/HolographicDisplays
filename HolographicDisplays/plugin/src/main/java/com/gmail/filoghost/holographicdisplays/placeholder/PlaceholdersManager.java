package com.gmail.filoghost.holographicdisplays.placeholder;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.task.WorldPlayerCounterTask;
import com.gmail.filoghost.holographicdisplays.util.Utils;

public class PlaceholdersManager {
	
	private static long elapsedTenthsOfSecond;
	protected static Set<DynamicLineData> linesToUpdate = Utils.newSet();
	
	private static final Pattern BUNGEE_ONLINE_PATTERN = makePlaceholderWithArgsPattern("online");
	private static final Pattern BUNGEE_MAX_PATTERN = makePlaceholderWithArgsPattern("max_players");
	private static final Pattern BUNGEE_MOTD_PATTERN = makePlaceholderWithArgsPattern("motd");
	private static final Pattern BUNGEE_MOTD_2_PATTERN = makePlaceholderWithArgsPattern("motd2");
	private static final Pattern BUNGEE_STATUS_PATTERN = makePlaceholderWithArgsPattern("status");
	private static final Pattern ANIMATION_PATTERN = makePlaceholderWithArgsPattern("animation");
	private static final Pattern WORLD_PATTERN = makePlaceholderWithArgsPattern("world");
	
	private static Pattern makePlaceholderWithArgsPattern(String prefix) {
		return Pattern.compile("(\\{" + Pattern.quote(prefix) + ":)(.+?)(\\})");
	}
	
	private static String extractArgumentFromPlaceholder(Matcher matcher) {
		return matcher.group(2).trim();
	}
	
	
	public static void load(Plugin plugin) {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				for (Placeholder placeholder : PlaceholdersRegister.getPlaceholders()) {
					if (elapsedTenthsOfSecond % placeholder.getTenthsToRefresh() == 0) {
						try {
							placeholder.update();
						} catch (Throwable t) {
							HolographicDisplays.getInstance().getLogger().log(Level.WARNING, "The placeholder " + placeholder.getTextPlaceholder() + " registered by the plugin " + placeholder.getOwner().getName() + " generated an exception while updating. Please contact the author of " + placeholder.getOwner().getName(), t);
						}
					}
				}
				
				for (Placeholder placeholder : AnimationsRegister.getAnimations().values()) {
					if (elapsedTenthsOfSecond % placeholder.getTenthsToRefresh() == 0) {
						placeholder.update();
					}
				}
				
				Iterator<DynamicLineData> iter = linesToUpdate.iterator();
				DynamicLineData currentLineData;
				
				while (iter.hasNext()) {
					currentLineData = iter.next();
					
					if (currentLineData.getEntity().isDeadNMS()) {
						iter.remove();
					} else {
						updatePlaceholders(currentLineData);
					}
				}
				
				elapsedTenthsOfSecond++;
			}
			
		}, 2L, 2L);
	}
	
	
	public static void untrackAll() {
		linesToUpdate.clear();
	}
	
	public static void untrack(CraftTextLine line) {
		
		if (line == null || !line.isSpawned()) {
			return;
		}
		
		Iterator<DynamicLineData> iter = linesToUpdate.iterator();
		while (iter.hasNext()) {
			DynamicLineData data = iter.next();
			if (data.getEntity() == line.getNmsNameble()) {
				iter.remove();
				data.getEntity().setCustomNameNMS(data.getOriginalName());
			}
		}
	}
	
	public static void trackIfNecessary(CraftTextLine line) {
		
		NMSNameable nameableEntity = line.getNmsNameble();
		String name = line.getText();
		
		if (nameableEntity == null) {
			return;
		}
		
		boolean updateName = false;
		
		if (name == null || name.isEmpty()) {
			return;
		}

		// Lazy initialization.
		Set<Placeholder> normalPlaceholders = null;
		
		Map<String, PlaceholderReplacer> bungeeReplacers = null;
		
		Map<String, PlaceholderReplacer> worldsOnlinePlayersReplacers = null;
		Map<String, Placeholder> animationsPlaceholders = null;
		
		Matcher matcher;
		
		for (Placeholder placeholder : PlaceholdersRegister.getPlaceholders()) {
			
			if (name.contains(placeholder.getTextPlaceholder())) {
				
				if (normalPlaceholders == null) {
					normalPlaceholders = Utils.newSet();
				}
				
				normalPlaceholders.add(placeholder);
			}
		}
		
		
		// Players in a world count pattern.
		matcher = WORLD_PATTERN.matcher(name);
		while (matcher.find()) {
							
			if (worldsOnlinePlayersReplacers == null) {
				worldsOnlinePlayersReplacers = Utils.newMap();
			}
							
			final String worldName = extractArgumentFromPlaceholder(matcher);
			worldsOnlinePlayersReplacers.put(matcher.group(), new PlaceholderReplacer() {
				
				@Override
				public String update() {
					return WorldPlayerCounterTask.getCount(worldName);
				}
			});
		}
		
		// BungeeCord online pattern.
		matcher = BUNGEE_ONLINE_PATTERN.matcher(name);
		while (matcher.find()) {
			
			if (bungeeReplacers == null) {
				bungeeReplacers = Utils.newMap();
			}
			
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			if (serverName.contains(",")) {
				
				String[] split = serverName.split(",");
				for (int i = 0; i < split.length; i++) {
					split[i] = split[i].trim();
				}
				
				final String[] serversToTrack = split;
			
				// Add it to tracked servers.
				bungeeReplacers.put(matcher.group(), new PlaceholderReplacer() {
					
					@Override
					public String update() {
						int count = 0;
						for (String serverToTrack : serversToTrack) {
							count += BungeeServerTracker.getPlayersOnline(serverToTrack);
						}
						return String.valueOf(count);
					}
				});
			} else {
				// Normal, single tracked server.
				bungeeReplacers.put(matcher.group(), new PlaceholderReplacer() {
					
					@Override
					public String update() {
						return String.valueOf(BungeeServerTracker.getPlayersOnline(serverName));
					}
				});
			}
		}
		
		// BungeeCord max players pattern.
		matcher = BUNGEE_MAX_PATTERN.matcher(name);
		while (matcher.find()) {
			
			if (bungeeReplacers == null) {
				bungeeReplacers = Utils.newMap();
			}
			
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			// Add it to tracked servers.
			bungeeReplacers.put(matcher.group(), new PlaceholderReplacer() {
				
				@Override
				public String update() {
					return BungeeServerTracker.getMaxPlayers(serverName);
				}
			});
		}
		
		// BungeeCord motd pattern.
		matcher = BUNGEE_MOTD_PATTERN.matcher(name);
		while (matcher.find()) {
			
			if (bungeeReplacers == null) {
				bungeeReplacers = Utils.newMap();
			}
			
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			// Add it to tracked servers.
			bungeeReplacers.put(matcher.group(), new PlaceholderReplacer() {
				
				@Override
				public String update() {
					return BungeeServerTracker.getMotd1(serverName);
				}
			});
		}
		
		// BungeeCord motd (line 2) pattern.
		matcher = BUNGEE_MOTD_2_PATTERN.matcher(name);
		while (matcher.find()) {
			
			if (bungeeReplacers == null) {
				bungeeReplacers = Utils.newMap();
			}
			
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			// Add it to tracked servers.
			bungeeReplacers.put(matcher.group(), new PlaceholderReplacer() {
				
				@Override
				public String update() {
					return BungeeServerTracker.getMotd2(serverName);
				}
			});
		}
		
		// BungeeCord status pattern.
		matcher = BUNGEE_STATUS_PATTERN.matcher(name);
		while (matcher.find()) {
			
			if (bungeeReplacers == null) {
				bungeeReplacers = Utils.newMap();
			}
			
			final String serverName = extractArgumentFromPlaceholder(matcher);
			BungeeServerTracker.track(serverName); // Track this server.
			
			// Add it to tracked servers.
			bungeeReplacers.put(matcher.group(), new PlaceholderReplacer() {
				
				@Override
				public String update() {
					return BungeeServerTracker.getOnlineStatus(serverName);
				}
			});
		}
		
		
		// Animation pattern.
		matcher = ANIMATION_PATTERN.matcher(name);
		while (matcher.find()) {

			String fileName = extractArgumentFromPlaceholder(matcher);
			Placeholder animation = AnimationsRegister.getAnimation(fileName);
			
			// If exists...
			if (animation != null) {
				
				if (animationsPlaceholders == null) {
					animationsPlaceholders = Utils.newMap();
				}
				
				animationsPlaceholders.put(matcher.group(), animation);
				
			} else {
				name = name.replace(matcher.group(), "[Animation not found: " + fileName + "]");
				updateName = true;
			}
		}
		
		if (Utils.isThereNonNull(normalPlaceholders, bungeeReplacers, worldsOnlinePlayersReplacers, animationsPlaceholders)) {

			DynamicLineData lineData = new DynamicLineData(nameableEntity, name);
			
			if (normalPlaceholders != null) {
				lineData.setPlaceholders(normalPlaceholders);
			}
			
			if (bungeeReplacers != null) {
				lineData.getReplacers().putAll(bungeeReplacers);
			}
			
			if (worldsOnlinePlayersReplacers != null) {
				lineData.getReplacers().putAll(worldsOnlinePlayersReplacers);
			}
			
			if (animationsPlaceholders != null) {
				lineData.getAnimations().putAll(animationsPlaceholders);
			}
			
			// It could be already tracked!
			if (!linesToUpdate.add(lineData)) {
				linesToUpdate.remove(lineData);
				linesToUpdate.add(lineData);
			}
			
			updatePlaceholders(lineData);
			
		} else {
			
			// The name needs to be updated anyways.
			if (updateName) {
				nameableEntity.setCustomNameNMS(name);
			}
		}
	}
	
	
	private static void updatePlaceholders(DynamicLineData lineData) {
		
		String oldCustomName = lineData.getEntity().getCustomNameNMS();
		String newCustomName = lineData.getOriginalName();
		
		if (!lineData.getPlaceholders().isEmpty()) {
			for (Placeholder placeholder : lineData.getPlaceholders()) {
				newCustomName = newCustomName.replace(placeholder.getTextPlaceholder(), Utils.sanitize(placeholder.getCurrentReplacement()));
			}
		}
		
		if (!lineData.getReplacers().isEmpty()) {
			for (Entry<String, PlaceholderReplacer> entry : lineData.getReplacers().entrySet()) {
				newCustomName = newCustomName.replace(entry.getKey(), Utils.sanitize(entry.getValue().update()));
			}
		}
		
		if (!lineData.getAnimations().isEmpty()) {
			for (Entry<String, Placeholder> entry : lineData.getAnimations().entrySet()) {
				newCustomName = newCustomName.replace(entry.getKey(), Utils.sanitize(entry.getValue().getCurrentReplacement()));
			}
		}
		
		// Update only if needed, don't send useless packets.
		if (!oldCustomName.equals(newCustomName)) {
			lineData.getEntity().setCustomNameNMS(newCustomName);
		}
	}

}
