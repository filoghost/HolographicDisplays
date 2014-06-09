package com.gmail.filoghost.holograms.placeholders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

import com.gmail.filoghost.holograms.Configuration;
import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.bungee.ServerInfoTimer;
import com.gmail.filoghost.holograms.nms.interfaces.HologramHorse;
import com.gmail.filoghost.holograms.object.HologramLineData;

public class PlaceholderManager {
	
	private static int taskID = -1;
	private static List<HologramLineData> horsesToRefresh;
	private static long elapsedLongTicks;
	
	private static final Pattern BUNGEE_ONLINE_PATTERN = Pattern.compile("(\\{online:)([^}]+)(\\})");
	private static final Pattern BUNGEE_STATUS_PATTERN = Pattern.compile("(\\{status:)([^}]+)(\\})");
	private static final Pattern ANIMATION_PATTERN = Pattern.compile("(\\{animation:)([^}]+)(\\})");
	
	public PlaceholderManager() {
		horsesToRefresh = new ArrayList<HologramLineData>();
		
		// Start the repeating tasks.
		startTask();
	}
	
	public void trackIfNecessary(HologramHorse horse) {
		
		String customName = horse.getCustomNameNMS();
		if (customName == null || customName.length() == 0) {
			return;
		}

		// TODO not really safe, could change in the future.
		if (!(customName.contains("{") && customName.contains("}")) && !customName.contains("&u")) {
			// All the placeholders have curly brackets or &u, optimization.
			return;
		}
		
		// Don't create a list if not necessary.
		List<Placeholder> containedPlaceholders = null;
		List<String> bungeeServersOnlinePlayers = null;
		List<String> bungeeServersStatuses = null;
		Matcher matcher;
		
		for (Placeholder placeholder : PlaceholdersList.getDefaults()) {
			
			if (customName.contains(placeholder.getLongPlaceholder())) {
				
				if (containedPlaceholders == null) {
					// Now we create a list, because at least one placeholder has been found.
					containedPlaceholders = new ArrayList<Placeholder>();
				}
				
				// Optimize calculations with shorter placeholders.
				customName = customName.replace(placeholder.getLongPlaceholder(), placeholder.getShortPlaceholder());
				
				// Add the placeholder to the list.
				containedPlaceholders.add(placeholder);
			}
			
		}
		
		// BungeeCord online pattern.
		matcher = BUNGEE_ONLINE_PATTERN.matcher(customName);
		while (matcher.find()) {
			
			if (bungeeServersOnlinePlayers == null) {
				bungeeServersOnlinePlayers = new ArrayList<String>();
			}
			
			String serverName = matcher.group(2).trim();
			ServerInfoTimer.track(serverName); // Track this server.
			
			// Shorter placeholder without spaces.
			customName = customName.replace(matcher.group(), "{online:" + serverName + "}");
			
			// Add it to tracked servers.
			bungeeServersOnlinePlayers.add(serverName);
		}
		
		// BungeeCord status pattern. 
		matcher = BUNGEE_STATUS_PATTERN.matcher(customName);
		while (matcher.find()) {
					
			if (bungeeServersStatuses == null) {
				bungeeServersStatuses = new ArrayList<String>();
			}
					
			String serverName = matcher.group(2).trim();
			ServerInfoTimer.track(serverName); // Track this server.
			
			// Shorter placeholder without spaces.
			customName = customName.replace(matcher.group(), "{status:" + serverName + "}");
			
			// Add it to tracked servers.
			bungeeServersStatuses.add(serverName);
		}
		
		
		
		
		// Animation pattern.
		matcher = ANIMATION_PATTERN.matcher(customName);
		boolean updateName = false;
		while (matcher.find()) {

			String fileName = matcher.group(2).trim();
			AnimatedPlaceholder animated = AnimationManager.getFromFilename(fileName);
			
			// If exists...
			if (animated != null) {

				customName = customName.replace(matcher.group(), "{animation:" + fileName + "}");
				
				if (containedPlaceholders == null) {
					// Now we create a list, because at least one placeholder has been found.
					containedPlaceholders = new ArrayList<Placeholder>();
				}
				
				containedPlaceholders.add(animated);
			} else {
				customName = customName.replace(matcher.group(), "{File not found: " + fileName + "}");
				updateName = true;
			}
		}
		
		if (containedPlaceholders != null || bungeeServersOnlinePlayers != null || bungeeServersStatuses != null) {
			HologramLineData data = new HologramLineData(horse, customName);
			
			if (containedPlaceholders != null) {
				data.setContainedPlaceholders(containedPlaceholders);
			}
			
			if (bungeeServersOnlinePlayers != null) {
				data.setBungeeOnlinePlayersToCheck(bungeeServersOnlinePlayers);
			}
			
			if (bungeeServersStatuses != null) {
				data.setBungeeStatusesToCheck(bungeeServersStatuses);
			}
			
			horsesToRefresh.add(data);
			updatePlaceholders(data);
		} else {
			
			// The name needs to be updated anyways.
			if (updateName) {
				horse.forceSetCustomName(customName);
			}
		}
	}
	
	public void startTask() {
		
		if (taskID != -1) {
			Bukkit.getScheduler().cancelTask(taskID);
		}
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(HolographicDisplays.getInstance(), new Runnable() {			
			
			public void run() {
				
				for (Placeholder placeholder : PlaceholdersList.getDefaults()) {
					if (elapsedLongTicks % placeholder.getTenthsToRefresh() == 0) {
						placeholder.update();
					}
				}
				
				for (Placeholder placeholder : PlaceholdersList.getAnimated()) {
					if (elapsedLongTicks % placeholder.getTenthsToRefresh() == 0) {
						placeholder.update();
					}
				}
				
				Iterator<HologramLineData> iter = horsesToRefresh.iterator();
				
				HologramLineData current;
				
				while (iter.hasNext()) {
					current = iter.next();
					
					if (current.getHorse().isDeadNMS()) {
						iter.remove();
					} else {
						updatePlaceholders(current);
					}
				}
				
				elapsedLongTicks++;
			}
			
		}, 2L, 2L); 
	}
	
	private void updatePlaceholders(HologramLineData data) {
		
		String oldCustomName = data.getHorse().getCustomNameNMS();
		String newCustomName = data.getSavedName();
		
		if (data.hasPlaceholders()) {
			for (Placeholder placeholder : data.getPlaceholders()) {
				newCustomName = newCustomName.replace(placeholder.getShortPlaceholder(), placeholder.getReplacement());
			}
		}
		
		if (data.hasBungeeOnlinePlayersToCheck()) {
			for (String server : data.getBungeeOnlinePlayersToCheck()) {
				newCustomName = newCustomName.replace("{online:" + server + "}", Integer.toString(ServerInfoTimer.getPlayersOnline(server)));
			}
		}
		
		if (data.hasBungeeStatusesToCheck()) {
			for (String server : data.getBungeeStatusesToCheck()) {
				newCustomName = newCustomName.replace("{status:" + server + "}", ServerInfoTimer.getOnlineStatus(server) ? Configuration.bungeeOnlineFormat : Configuration.bungeeOfflineFormat);
			}
		}
		
		// Update only if needed, don't send useless packets.
		if (!oldCustomName.equals(newCustomName)) {
			data.getHorse().forceSetCustomName(newCustomName);
		}
	}
}
