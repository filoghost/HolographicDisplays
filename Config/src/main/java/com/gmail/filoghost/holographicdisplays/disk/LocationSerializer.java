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
		decimalFormat = new DecimalFormat("0.000");
		DecimalFormatSymbols formatSymbols = decimalFormat.getDecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(formatSymbols);
	}

	public static Location locationFromString(String input) throws WorldNotFoundException, InvalidFormatException {
		if (input == null) {
			throw new InvalidFormatException();
		}
		
		String[] parts = input.split(",");
		
		if (parts.length != 4) {
			throw new InvalidFormatException();
		}
		
		try {
			double x = Double.parseDouble(parts[1].replace(" ", ""));
			double y = Double.parseDouble(parts[2].replace(" ", ""));
			double z = Double.parseDouble(parts[3].replace(" ", ""));
		
			World world = Bukkit.getWorld(parts[0].trim());
			if (world == null) {
				throw new WorldNotFoundException(parts[0].trim());
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
