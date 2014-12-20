package com.gmail.filoghost.holographicdisplays.disk;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.gmail.filoghost.holographicdisplays.exception.InvalidFormatException;
import com.gmail.filoghost.holographicdisplays.exception.WorldNotFoundException;

public class LocationSerializer {
	
	private static DecimalFormat decimalFormat;
	static {
		// More precision is not needed at all.
		decimalFormat = new DecimalFormat("0.0000");
		DecimalFormatSymbols formatSymbols = decimalFormat.getDecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(formatSymbols);
	}

	public static Location locationFromString(String input) throws WorldNotFoundException, InvalidFormatException {
		if (input == null) {
			throw new InvalidFormatException();
		}
		
		String[] parts = input.replace(" ", "").split(",");
		
		if (parts.length != 4) {
			throw new InvalidFormatException();
		}
		
		try {
			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);
		
			World world = Bukkit.getWorld(parts[0]);
			if (world == null) {
				throw new WorldNotFoundException(parts[0]);
			}
			
			return new Location(world, x, y, z);
			
		} catch (NumberFormatException ex) {
			throw new InvalidFormatException();
		}
	}
	
	public static String locationToString(Location loc) {
		return (loc.getWorld().getName() + ", " + decimalFormat.format(loc.getX()) + ", " + decimalFormat.format(loc.getY()) + ", " + decimalFormat.format(loc.getZ()));
	}
}
