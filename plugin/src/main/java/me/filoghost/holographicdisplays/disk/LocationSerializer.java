/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LocationSerializer {
    
    private static DecimalFormat numberFormat = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));

    public static Location locationFromString(String hologramName, String serializedLocation) throws LocationWorldNotLoadedException, LocationFormatException {
        if (serializedLocation == null) {
            throw new LocationFormatException("hologram \"" + hologramName + "\" doesn't have a location set");
        }

        String[] parts = Strings.splitAndTrim(serializedLocation, ",");

        if (parts.length != 4) {
            throw new LocationFormatException("hologram \"" + hologramName + "\" has an invalid location format:" 
                    + " it must be \"world, x, y, z\"");
        }
        
        try {
            String worldName = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                throw new LocationWorldNotLoadedException("hologram \"" + hologramName + "\"" 
                        + " was in the world \"" + worldName + "\" but it wasn't loaded");
            }
            
            return new Location(world, x, y, z);
            
        } catch (NumberFormatException ex) {
            throw new LocationFormatException("hologram \"" + hologramName + "\"" 
                    + " has an invalid location format: invalid number");
        }
    }
    
    public static String locationToString(Location loc) {
        return (loc.getWorld().getName() + ", " + numberFormat.format(loc.getX()) + ", " + numberFormat.format(loc.getY()) + ", " + numberFormat.format(loc.getZ()));
    }
}
