/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.bridge.bungeecord.BungeeServerTracker;
import me.filoghost.holographicdisplays.core.Utils;
import me.filoghost.holographicdisplays.core.hologram.StandardTextLine;
import me.filoghost.holographicdisplays.core.nms.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.task.WorldPlayerCounterTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholdersManager {
    
    private static final Pattern BUNGEE_ONLINE_PATTERN = makePlaceholderWithArgsPattern("online");
    private static final Pattern BUNGEE_MAX_PATTERN = makePlaceholderWithArgsPattern("max_players");
    private static final Pattern BUNGEE_MOTD_PATTERN = makePlaceholderWithArgsPattern("motd");
    private static final Pattern BUNGEE_MOTD_2_PATTERN = makePlaceholderWithArgsPattern("motd2");
    private static final Pattern BUNGEE_STATUS_PATTERN = makePlaceholderWithArgsPattern("status");
    private static final Pattern ANIMATION_PATTERN = makePlaceholderWithArgsPattern("animation");
    private static final Pattern WORLD_PATTERN = makePlaceholderWithArgsPattern("world");

    private final PlaceholdersRegistry placeholderRegistry;
    private final AnimationsRegistry animationRegistry;
    private final BungeeServerTracker bungeeServerTracker;
    protected final Set<DynamicLineData> linesToUpdate;
    private long elapsedTenthsOfSecond;

    public PlaceholdersManager(BungeeServerTracker bungeeServerTracker, AnimationsRegistry animationRegistry) {
        this.placeholderRegistry = new PlaceholdersRegistry(this);
        this.animationRegistry = animationRegistry;
        this.bungeeServerTracker = bungeeServerTracker;
        this.linesToUpdate = new HashSet<>();
        
        placeholderRegistry.addDefaultPlaceholders();
    }

    public void startRefreshTask(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            
            for (Placeholder placeholder : placeholderRegistry.getPlaceholders()) {
                if (elapsedTenthsOfSecond % placeholder.getTenthsToRefresh() == 0) {
                    try {
                        placeholder.update();
                    } catch (Throwable t) {
                        Log.warning("The placeholder " + placeholder.getTextPlaceholder() + " registered by the plugin " + placeholder.getOwner().getName() + " generated an exception while updating. Please contact the author of " + placeholder.getOwner().getName(), t);
                    }
                }
            }
            
            for (Placeholder placeholder : animationRegistry.getAnimationsByFilename().values()) {
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
            
        }, 2L, 2L);
    }
    
    
    public void untrackAll() {
        linesToUpdate.clear();
    }
    
    public void untrack(StandardTextLine line) {
        Iterator<DynamicLineData> iter = linesToUpdate.iterator();
        while (iter.hasNext()) {
            DynamicLineData data = iter.next();
            if (data.getEntity() == line.getNMSArmorStand()) {
                iter.remove();
                data.getEntity().setCustomNameNMS(data.getOriginalName());
            }
        }
    }
    
    public void trackIfNecessary(StandardTextLine line) {        
        String text = line.getText();
        if (text == null || text.isEmpty()) {
            return;
        }
        
        NMSArmorStand entity = line.getNMSArmorStand();
        if (entity == null) {
            return;
        }        
        
        boolean updateText = false;

        // Lazy initialization.
        Set<Placeholder> normalPlaceholders = null;
        Map<String, PlaceholderReplacer> bungeeReplacers = null;
        Map<String, PlaceholderReplacer> worldsOnlinePlayersReplacers = null;
        Map<String, Placeholder> animationsPlaceholders = null;
        
        Matcher matcher;
        
        for (Placeholder placeholder : placeholderRegistry.getPlaceholders()) {
            if (text.contains(placeholder.getTextPlaceholder())) {
                if (normalPlaceholders == null) {
                    normalPlaceholders = new HashSet<>();
                }
                normalPlaceholders.add(placeholder);
            }
        }
        
        
        // Players in a world count pattern.
        matcher = WORLD_PATTERN.matcher(text);
        while (matcher.find()) {
            if (worldsOnlinePlayersReplacers == null) {
                worldsOnlinePlayersReplacers = new HashMap<>();
            }
                            
            final String worldsNames = extractArgumentFromPlaceholder(matcher);
            
            if (worldsNames.contains(",")) {
                String[] split = worldsNames.split(",");
                for (int i = 0; i < split.length; i++) {
                    split[i] = split[i].trim();
                }
                
                final String[] worldsToTrack = split;
            
                // Add it to tracked worlds.
                worldsOnlinePlayersReplacers.put(matcher.group(), () -> {
                    return WorldPlayerCounterTask.getCount(worldsToTrack);
                });
            } else {
                // Normal, single tracked world.
                worldsOnlinePlayersReplacers.put(matcher.group(), () -> {
                    return WorldPlayerCounterTask.getCount(worldsNames);
                });
            }
        }
        
        // BungeeCord online pattern.
        matcher = BUNGEE_ONLINE_PATTERN.matcher(text);
        while (matcher.find()) {
            if (bungeeReplacers == null) {
                bungeeReplacers = new HashMap<>();
            }
            
            final String serverName = extractArgumentFromPlaceholder(matcher);
            bungeeServerTracker.track(serverName); // Track this server.
            
            if (serverName.contains(",")) {
                String[] split = serverName.split(",");
                for (int i = 0; i < split.length; i++) {
                    split[i] = split[i].trim();
                }
                
                final String[] serversToTrack = split;
            
                // Add it to tracked servers.
                bungeeReplacers.put(matcher.group(), () -> {
                    int count = 0;
                    for (String serverToTrack : serversToTrack) {
                        count += bungeeServerTracker.getPlayersOnline(serverToTrack);
                    }
                    return String.valueOf(count);
                });
            } else {
                // Normal, single tracked server.
                bungeeReplacers.put(matcher.group(), () -> {
                    return String.valueOf(bungeeServerTracker.getPlayersOnline(serverName));
                });
            }
        }
        
        // BungeeCord max players pattern.
        matcher = BUNGEE_MAX_PATTERN.matcher(text);
        while (matcher.find()) {
            if (bungeeReplacers == null) {
                bungeeReplacers = new HashMap<>();
            }
            
            final String serverName = extractArgumentFromPlaceholder(matcher);
            bungeeServerTracker.track(serverName); // Track this server.
            
            // Add it to tracked servers.
            bungeeReplacers.put(matcher.group(), () -> {
                return bungeeServerTracker.getMaxPlayers(serverName);
            });
        }
        
        // BungeeCord motd pattern.
        matcher = BUNGEE_MOTD_PATTERN.matcher(text);
        while (matcher.find()) {
            if (bungeeReplacers == null) {
                bungeeReplacers = new HashMap<>();
            }
            
            final String serverName = extractArgumentFromPlaceholder(matcher);
            bungeeServerTracker.track(serverName); // Track this server.
            
            // Add it to tracked servers.
            bungeeReplacers.put(matcher.group(), () -> {
                return bungeeServerTracker.getMotd1(serverName);
            });
        }
        
        // BungeeCord motd (line 2) pattern.
        matcher = BUNGEE_MOTD_2_PATTERN.matcher(text);
        while (matcher.find()) {
            if (bungeeReplacers == null) {
                bungeeReplacers = new HashMap<>();
            }
            
            final String serverName = extractArgumentFromPlaceholder(matcher);
            bungeeServerTracker.track(serverName); // Track this server.
            
            // Add it to tracked servers.
            bungeeReplacers.put(matcher.group(), () -> {
                return bungeeServerTracker.getMotd2(serverName);
            });
        }
        
        // BungeeCord status pattern.
        matcher = BUNGEE_STATUS_PATTERN.matcher(text);
        while (matcher.find()) {
            if (bungeeReplacers == null) {
                bungeeReplacers = new HashMap<>();
            }
            
            final String serverName = extractArgumentFromPlaceholder(matcher);
            bungeeServerTracker.track(serverName); // Track this server.
            
            // Add it to tracked servers.
            bungeeReplacers.put(matcher.group(), () -> {
                return bungeeServerTracker.getOnlineStatus(serverName);
            });
        }
        
        
        // Animation pattern.
        matcher = ANIMATION_PATTERN.matcher(text);
        while (matcher.find()) {
            String fileName = extractArgumentFromPlaceholder(matcher);
            Placeholder animation = animationRegistry.getAnimation(fileName);
            
            // If exists...
            if (animation != null) {
                if (animationsPlaceholders == null) {
                    animationsPlaceholders = new HashMap<>();
                }
                
                animationsPlaceholders.put(matcher.group(), animation);
                
            } else {
                text = text.replace(matcher.group(), "[Animation not found: " + fileName + "]");
                updateText = true;
            }
        }
        
        if (Utils.isThereNonNull(normalPlaceholders, bungeeReplacers, worldsOnlinePlayersReplacers, animationsPlaceholders)) {
            DynamicLineData lineData = new DynamicLineData(entity, text);
            
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
            if (updateText) {
                entity.setCustomNameNMS(text);
            }
        }
    }
    
    
    private void updatePlaceholders(DynamicLineData lineData) {
        String oldCustomName = lineData.getEntity().getCustomNameStringNMS();
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

    public PlaceholdersRegistry getRegistry() {
        return placeholderRegistry;
    }

    private static Pattern makePlaceholderWithArgsPattern(String prefix) {
        return Pattern.compile("(\\{" + Pattern.quote(prefix) + ":)(.+?)(\\})");
    }

    private static String extractArgumentFromPlaceholder(Matcher matcher) {
        return matcher.group(2).trim();
    }

}
