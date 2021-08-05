/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.holographicdisplays.plugin.hologram.base.BaseHologramPosition;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologram;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HologramConfig {

    private static final DecimalFormat POSITION_NUMBER_FORMAT
            = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));

    private final String name;
    private final List<String> serializedLines;
    private final String serializedPosition;

    public HologramConfig(String name, ConfigSection configSection) {
        this.name = name;
        this.serializedLines = configSection.getStringList("lines");
        this.serializedPosition = configSection.getString("location");
    }

    public HologramConfig(InternalHologram hologram) {
        this.name = hologram.getName();
        this.serializedLines = new ArrayList<>();
        for (InternalHologramLine line : hologram.getLines()) {
            serializedLines.add(line.getSerializedConfigValue());
        }

        this.serializedPosition = serializePosition(hologram.getBasePosition());
    }

    public ConfigSection toConfigSection() {
        ConfigSection configSection = new ConfigSection();
        configSection.setStringList("lines", serializedLines);
        configSection.setString("location", serializedPosition);
        return configSection;
    }

    public InternalHologram createHologram(InternalHologramManager internalHologramManager) throws HologramLoadException {
        if (serializedLines == null || serializedLines.size() == 0) {
            throw new HologramLoadException("at least one line is required");
        }
        if (serializedPosition == null) {
            throw new HologramLoadException("no location set");
        }

        BaseHologramPosition position = deserializePosition(serializedPosition);
        InternalHologram hologram = internalHologramManager.createHologram(position, name);
        List<InternalHologramLine> lines = new ArrayList<>();

        for (String serializedLine : serializedLines) {
            try {
                lines.add(HologramLineParser.parseLine(hologram, serializedLine));
            } catch (HologramLoadException e) {
                // Rethrow with more details
                throw new HologramLoadException("invalid line: " + e.getMessage(), e);
            }
        }

        hologram.setLines(lines);
        return hologram;
    }

    private String serializePosition(BaseHologramPosition position) {
        return position.getWorld().getName()
                + ", " + POSITION_NUMBER_FORMAT.format(position.getX())
                + ", " + POSITION_NUMBER_FORMAT.format(position.getY())
                + ", " + POSITION_NUMBER_FORMAT.format(position.getZ());
    }

    private BaseHologramPosition deserializePosition(String serializedPosition) throws HologramLoadException {
        String[] parts = Strings.splitAndTrim(serializedPosition, ",");

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

            return new BaseHologramPosition(world, x, y, z);

        } catch (NumberFormatException ex) {
            throw new HologramLoadException("hologram \"" + name + "\""
                    + " has an invalid location format: invalid number");
        }
    }

    public String getName() {
        return name;
    }

}
