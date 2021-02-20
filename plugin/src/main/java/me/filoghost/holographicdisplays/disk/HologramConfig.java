/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.holographicdisplays.object.InternalHologram;
import me.filoghost.holographicdisplays.object.InternalHologramManager;
import me.filoghost.holographicdisplays.object.line.HologramLineImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HologramConfig {

    private static final DecimalFormat LOCATION_NUMBER_FORMAT 
            = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));
    
    private final String name;
    private final List<String> serializedLines;
    private final String serializedLocation;

    public HologramConfig(String name, ConfigSection configSection) {
        this.name = name;
        this.serializedLines = configSection.getStringList("lines");
        this.serializedLocation = configSection.getString("location");
    }

    public HologramConfig(InternalHologram hologram) {
        this.name = hologram.getName();
        this.serializedLines = new ArrayList<>();
        for (HologramLineImpl line : hologram.getLinesUnsafe()) {
            serializedLines.add(line.getSerializedConfigValue());
        }

       this.serializedLocation = serializeLocation(hologram.getLocation());
    }

    public ConfigSection toConfigSection() {
        ConfigSection configSection = new ConfigSection();
        configSection.setStringList("lines", serializedLines);
        configSection.setString("location", serializedLocation);
        return configSection;
    }

    public InternalHologram createHologram(InternalHologramManager internalHologramManager) throws HologramLoadException {
        if (serializedLines == null || serializedLines.size() == 0) {
            throw new HologramLoadException("hologram \"" + name + "\" was found, but it contained no lines");
        }
        if (serializedLocation == null) {
            throw new HologramLoadException("hologram \"" + name + "\" doesn't have a location set");
        }

        Location location = deserializeLocation(serializedLocation);
        InternalHologram hologram = internalHologramManager.createHologram(location, name);

        for (String serializedLine : serializedLines) {
            try {
                HologramLineImpl line = HologramLineParser.parseLine(hologram, serializedLine, false);
                hologram.getLinesUnsafe().add(line);
            } catch (HologramLoadException e) {
                // Rethrow with more details
                throw new HologramLoadException("hologram \"" + hologram.getName() + "\" has an invalid line: " + e.getMessage(), e);
            }
        }

        return hologram;
    }

    private String serializeLocation(Location loc) {
        return loc.getWorld().getName()
                + ", " + LOCATION_NUMBER_FORMAT.format(loc.getX())
                + ", " + LOCATION_NUMBER_FORMAT.format(loc.getY())
                + ", " + LOCATION_NUMBER_FORMAT.format(loc.getZ());
    }

    private Location deserializeLocation(String serializedLocation) throws HologramLoadException {
        String[] parts = Strings.splitAndTrim(serializedLocation, ",");

        if (parts.length != 4) {
            throw new HologramLoadException("hologram \"" + name + "\" has an invalid location format:"
                    + " it must be \"world, x, y, z\"");
        }

        try {
            String worldName = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                throw new HologramLoadException("hologram \"" + name + "\""
                        + " was in the world \"" + worldName + "\" but it wasn't loaded");
            }

            return new Location(world, x, y, z);

        } catch (NumberFormatException ex) {
            throw new HologramLoadException("hologram \"" + name + "\""
                    + " has an invalid location format: invalid number");
        }
    }

    public String getName() {
        return name;
    }

}
