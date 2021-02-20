/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldPlayerCounterTask implements Runnable {

    private static final Map<String, Integer> worlds = new HashMap<>();
    
    @Override
    public void run() {
        worlds.clear();
        
        for (World world : Bukkit.getWorlds()) {
            List<Player> players = world.getPlayers();
            int count = 0;
            
            for (Player player : players) {
                if (!player.hasMetadata("NPC")) {
                    count++;
                }
            }
            worlds.put(world.getName(), count);
        }
    }
    
    public static String getCount(String[] worldsNames) {
        int total = 0;
        for (String worldName : worldsNames) {
            Integer count = worlds.get(worldName);
            if (count == null) {
                return "[World \"" + worldName + "\" not found]";
            }
            
            total += count;
        }
        
        return String.valueOf(total);
    }
    
    public static String getCount(String worldName) {
        Integer count = worlds.get(worldName);
        return count != null ? count.toString() : "[World \"" + worldName + "\" not found]";
    }
}
