package com.gmail.filoghost.holograms.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.gmail.filoghost.holograms.exception.WorldNotFoundException;

public class LocationUtils {

	public static Location locationFromString(String input) throws WorldNotFoundException, Exception {
		String[] parts = input.split(",");
		double x = Double.parseDouble(parts[1]);
		double y = Double.parseDouble(parts[2]);
		double z = Double.parseDouble(parts[3]);
		
		World world = Bukkit.getWorld(parts[0]);
		if (world == null) {
			throw new WorldNotFoundException(parts[0]);
		}
			
		return new Location(world, x, y, z);
	}
	
	public static String locationToString(Location loc) {
		return (loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ());
	}
}
