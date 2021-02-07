/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HologramDatabase {
    
    private static File file;
    private static FileConfiguration config;
    
    public static void loadYamlFile(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "database.yml");
        
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("database.yml", true);
        }
        
        config = YamlConfiguration.loadConfiguration(file);
    }
    
    public static NamedHologram loadHologram(String name) throws HologramNotFoundException, LocationFormatException, LocationWorldNotLoadedException, HologramLineParseException {
        ConfigurationSection configSection = config.getConfigurationSection(name);
        
        if (configSection == null) {
            throw new HologramNotFoundException("hologram \"" + name + "\" not found, skipping it");
        }
        
        List<String> lines = configSection.getStringList("lines");
        String locationString = configSection.getString("location");
        
        if (lines == null || locationString == null || lines.size() == 0) {
            throw new HologramNotFoundException("hologram \"" + name + "\" was found, but it contained no lines");
        }
        
        Location loc = LocationSerializer.locationFromString(name, locationString);
        
        NamedHologram hologram = new NamedHologram(loc, name);

        for (String line : lines) {
            try {
                hologram.getLinesUnsafe().add(HologramLineParser.parseLine(hologram, line, false));
            } catch (HologramLineParseException e) {
                throw new HologramLineParseException("hologram \"" + hologram.getName() + "\" has an invalid line: " + e.getMessage(), e.getCause());
            }
        }
        
        return hologram;
    }
    
    public static String serializeHologramLine(CraftHologramLine line) {
        return line.getSerializedConfigValue();
    }
    
    public static void deleteHologram(String name) {
        config.set(name, null);
    }
    
    public static void saveHologram(NamedHologram hologram) {        
        List<String> serializedLines = new ArrayList<>();
        for (CraftHologramLine line : hologram.getLinesUnsafe()) {
            serializedLines.add(serializeHologramLine(line));
        }
        
        ConfigurationSection hologramSection = getOrCreateSection(hologram.getName());        
        hologramSection.set("location", LocationSerializer.locationToString(hologram.getLocation()));
        hologramSection.set("lines", serializedLines);
    }
    
    public static Set<String> getHolograms() {
        return config.getKeys(false);
    }
    
    private static ConfigurationSection getOrCreateSection(String name) {
        if (config.isConfigurationSection(name)) {
            return config.getConfigurationSection(name);
        } else {
            return config.createSection(name);
        }
    }
    
    public static void saveToDisk() throws IOException {
        if (config != null && file != null) {
            config.save(file);
        }
    }
    
    public static void trySaveToDisk() {
        try {
            saveToDisk();
        } catch (IOException ex) {
            Log.severe("Unable to save database.yml to disk!", ex);
        }
    }
}
