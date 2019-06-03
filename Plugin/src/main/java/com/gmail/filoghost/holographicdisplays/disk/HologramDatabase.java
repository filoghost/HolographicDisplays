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
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicdisplays.exception.InvalidFormatException;
import com.gmail.filoghost.holographicdisplays.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.line.CraftHologramLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftItemLine;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTextLine;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.ItemUtils;

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
	
	public static NamedHologram loadHologram(String name) throws HologramNotFoundException, InvalidFormatException, WorldNotFoundException {
		
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
		for (int i = 0; i < lines.size(); i++) {
			hologram.getLinesUnsafe().add(readLineFromString(lines.get(i), hologram));
		}
		
		return hologram;
	}
	
	public static CraftHologramLine readLineFromString(String rawText, CraftHologram hologram) {
		if (rawText.toLowerCase().startsWith("icon:")) {
			String iconMaterial = ItemUtils.stripSpacingChars(rawText.substring("icon:".length(), rawText.length()));
			
			short dataValue = 0;
			
			if (iconMaterial.contains(":")) {
				try {
					dataValue = (short) Integer.parseInt(iconMaterial.split(":")[1]);
				} catch (NumberFormatException e) {	}
				iconMaterial = iconMaterial.split(":")[0];
			}
			
			Material mat = ItemUtils.matchMaterial(iconMaterial);
			if (mat == null) {
				mat = Material.BEDROCK;
			}
			
			return new CraftItemLine(hologram, new ItemStack(mat, 1, dataValue));
			
		} else {
			
			if (rawText.trim().equalsIgnoreCase("{empty}")) {
				return new CraftTextLine(hologram, "");
			} else {
				return new CraftTextLine(hologram, StringConverter.toReadableFormat(rawText));
			}
		}
	}
	
	public static String saveLineToString(CraftHologramLine line) {
		if (line instanceof CraftTextLine) {
			return StringConverter.toSaveableFormat(((CraftTextLine) line).getText());
			
		} else if (line instanceof CraftItemLine) {
			CraftItemLine itemLine = (CraftItemLine) line;
			return "ICON: " + itemLine.getItemStack().getType().toString().replace("_", " ").toLowerCase() + (itemLine.getItemStack().getDurability() != 0 ? ":" + itemLine.getItemStack().getDurability() : "");
		} else {
			
			return "Unknown";
		}
	}
	
	public static void deleteHologram(String name) {
		config.set(name, null);
	}
	
	public static void saveHologram(NamedHologram hologram) {
		
		ConfigurationSection configSection = config.isConfigurationSection(hologram.getName()) ? config.getConfigurationSection(hologram.getName()) : config.createSection(hologram.getName());
		
		configSection.set("location", LocationSerializer.locationToString(hologram.getLocation()));
		List<String> lines = new ArrayList<>();
		
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			
			lines.add(saveLineToString(line));
		}
		
		configSection.set("lines", lines);
	}
	
	public static Set<String> getHolograms() {
		return config.getKeys(false);
	}
	
	public static boolean isExistingHologram(String name) {
		return config.isConfigurationSection(name);
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
			ex.printStackTrace();
			ConsoleLogger.log(Level.SEVERE, "Unable to save database.yml to disk!");
		}
	}
}
