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
package com.gmail.filoghost.holographicdisplays.disk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicdisplays.exception.InvalidFormatException;
import com.gmail.filoghost.holographicdisplays.exception.HologramLineParseException;
import com.gmail.filoghost.holographicdisplays.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;

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
	
	public static NamedHologram loadHologram(String name) throws HologramNotFoundException, InvalidFormatException, WorldNotFoundException, HologramLineParseException {
		ConfigurationSection configSection = config.getConfigurationSection(name);
		
		if (configSection == null) {
			throw new HologramNotFoundException();
		}
		
		List<String> lines = configSection.getStringList("lines");
		String locationString = configSection.getString("location");
		
		if (lines == null || locationString == null || lines.size() == 0) {
			throw new HologramNotFoundException();
		}
		
		Location loc = LocationSerializer.locationFromString(locationString);
		
		NamedHologram hologram = new NamedHologram(loc, name);

		for (String line : lines) {
			hologram.getLinesUnsafe().add(HologramLineParser.parseLine(hologram, line, false));
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
	
	public static boolean isExistingHologram(String name) {
		return config.isConfigurationSection(name);
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
			ConsoleLogger.log(Level.SEVERE, "Unable to save database.yml to disk!", ex);
		}
	}
}
