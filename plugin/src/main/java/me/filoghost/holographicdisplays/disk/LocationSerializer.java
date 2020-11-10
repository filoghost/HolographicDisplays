/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.holographicdisplays.exception.InvalidFormatException;
import me.filoghost.holographicdisplays.exception.WorldNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LocationSerializer {
    
    private static DecimalFormat numberFormat = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));

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
        return (loc.getWorld().getName() + ", " + numberFormat.format(loc.getX()) + ", " + numberFormat.format(loc.getY()) + ", " + numberFormat.format(loc.getZ()));
    }
}
