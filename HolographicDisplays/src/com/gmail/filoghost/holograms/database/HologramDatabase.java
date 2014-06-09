package com.gmail.filoghost.holograms.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.exception.HologramNotFoundException;
import com.gmail.filoghost.holograms.exception.InvalidLocationException;
import com.gmail.filoghost.holograms.exception.WorldNotFoundException;
import com.gmail.filoghost.holograms.object.CraftHologram;
import com.gmail.filoghost.holograms.utils.LocationUtils;
import com.gmail.filoghost.holograms.utils.StringUtils;

public class HologramDatabase {

	private static File file;
	private static FileConfiguration config;
	
	private HologramDatabase() { }
	
	public static void initialize() {
		file = new File(HolographicDisplays.getInstance().getDataFolder(), "database.yml");
		if (!file.exists()) {
			HolographicDisplays.getInstance().saveResource("database.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static CraftHologram loadHologram(String name) throws HologramNotFoundException, InvalidLocationException, WorldNotFoundException {
		List<String> lines = config.getStringList(name + ".lines");
		String locationString = config.getString(name + ".location");
		if (lines == null || locationString == null || lines.size() == 0) {
			throw new HologramNotFoundException();
		}
		
		Location loc;
		try {
			loc = LocationUtils.locationFromString(locationString);
		} catch (WorldNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InvalidLocationException();
		}
		
		CraftHologram hologram = new CraftHologram(name, loc);
		for (int i = 0; i < lines.size(); i++) {
			hologram.addLine(StringUtils.toReadableFormat(lines.get(i)));
		}
		
		return hologram;
	}
	
	public static void deleteHologram(CraftHologram hologram) {
		config.set(hologram.getName(), null);
	}
	
	public static void saveHologram(CraftHologram hologram) {
		config.set(hologram.getName() + ".location", LocationUtils.locationToString(hologram.getLocation()));
		List<String> lines = new ArrayList<String>();
		for (String hologramLine : hologram.getLines()) {
			lines.add(StringUtils.toSaveableFormat(hologramLine));
		}
		config.set(hologram.getName() + ".lines", lines);
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
			HolographicDisplays.getInstance().getLogger().severe("Unable to save database.yml to disk!");
		}
	}
}
