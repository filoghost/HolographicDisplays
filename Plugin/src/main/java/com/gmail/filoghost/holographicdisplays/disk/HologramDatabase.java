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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
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
import com.gmail.filoghost.holographicdisplays.util.nbt.parser.MojangsonParseException;
import com.gmail.filoghost.holographicdisplays.util.nbt.parser.MojangsonParser;

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
			hologram.getLinesUnsafe().add(deserializeHologramLine(lines.get(i), hologram));
		}
		
		return hologram;
	}
	
	public static CraftHologramLine deserializeHologramLine(String rawText, CraftHologram hologram) {
		CraftHologramLine hologramLine;
		
		if (rawText.toLowerCase().startsWith("icon:")) {
			String serializedIcon = rawText.substring("icon:".length(), rawText.length());
			ItemStack icon = parseItemStack(serializedIcon);
			hologramLine = new CraftItemLine(hologram, icon);
			
		} else {
			if (rawText.trim().equalsIgnoreCase("{empty}")) {
				hologramLine = new CraftTextLine(hologram, "");
			} else {
				hologramLine = new CraftTextLine(hologram, StringConverter.toReadableFormat(rawText));
			}
		}
		
		hologramLine.setSerializedConfigValue(rawText);
		return hologramLine;
	}
	
	@SuppressWarnings("deprecation")
	private static ItemStack parseItemStack(String serializedItem) {
		serializedItem = serializedItem.trim();
		
		// Parse json
		int nbtStart = serializedItem.indexOf('{');
		int nbtEnd = serializedItem.lastIndexOf('}');
		String nbtString = null;
		
		String basicItemData;
		
		if (nbtStart > 0 && nbtEnd > 0 && nbtEnd > nbtStart) {
			nbtString = serializedItem.substring(nbtStart, nbtEnd + 1);
			basicItemData = serializedItem.substring(0, nbtStart) + serializedItem.substring(nbtEnd + 1, serializedItem.length());
		} else {
			basicItemData = serializedItem;
		}
		
		basicItemData = ItemUtils.stripSpacingChars(basicItemData);

		String materialName;
		short dataValue = 0;
		
		if (basicItemData.contains(":")) {
			String[] materialAndDataValue = basicItemData.split(":", -1);
			try {
				dataValue = (short) Integer.parseInt(materialAndDataValue[1]);
			} catch (NumberFormatException e) {
				HolographicDisplays.getInstance().getLogger().log(Level.WARNING, "Could not set data value for the item \"" + basicItemData + "\": invalid number.");
			}
			materialName = materialAndDataValue[0];
		} else {
			materialName = basicItemData;
		}
		
		Material material = ItemUtils.matchMaterial(materialName);
		if (material == null) {
			material = Material.BEDROCK;
		}
		
		ItemStack itemStack = new ItemStack(material, 1, dataValue);
		
		if (nbtString != null) {
			try {
				// Check NBT syntax validity before applying it.
				MojangsonParser.parse(nbtString);
				Bukkit.getUnsafe().modifyItemStack(itemStack, nbtString);
			} catch (MojangsonParseException e) {
				HolographicDisplays.getInstance().getLogger().log(Level.WARNING, "Invalid NBT data \"" + nbtString + "\" for the item \"" + basicItemData + "\": " + e.getMessage());
			} catch (Throwable t) {
				HolographicDisplays.getInstance().getLogger().log(Level.WARNING, "Could not apply NBT data \"" + nbtString + "\" to the item \"" + basicItemData + "\".", t);
			}
		}
		
		return itemStack;
	}
	
	public static String serializeHologramLine(CraftHologramLine line) {
		return line.getSerializedConfigValue();
	}
	
	public static void deleteHologram(String name) {
		config.set(name, null);
	}
	
	public static void saveHologram(NamedHologram hologram) {
		ConfigurationSection hologramSection = getOrCreateSection(hologram.getName());		
		hologramSection.set("location", LocationSerializer.locationToString(hologram.getLocation()));
		
		List<String> lines = new ArrayList<>();
		for (CraftHologramLine line : hologram.getLinesUnsafe()) {
			lines.add(serializeHologramLine(line));
		}
		
		hologramSection.set("lines", lines);
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
			ex.printStackTrace();
			ConsoleLogger.log(Level.SEVERE, "Unable to save database.yml to disk!");
		}
	}
}
