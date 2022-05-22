/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.collection.CollectionUtils;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.exception.ConfigValueException;
import me.filoghost.holographicdisplays.api.beta.Position;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;

import java.util.ArrayList;
import java.util.List;

public class InternalHologramConfig {

    private final String name;
    private final ConfigSection configSection;

    public InternalHologramConfig(String name, ConfigSection configSection) {
        this.name = name;
        this.configSection = configSection;
    }

    public InternalHologramConfig(InternalHologram hologram) {
        this.name = hologram.getName();
        this.configSection = new ConfigSection();

        List<String> serializedLines = serializeLines(hologram);
        ConfigSection positionConfigSection = serializePosition(hologram.getPosition());

        configSection.setStringList("lines", serializedLines);
        configSection.setConfigSection("position", positionConfigSection);
    }

    public String getName() {
        return name;
    }

    public ConfigSection getSerializedConfigSection() {
        return configSection;
    }

    private List<String> serializeLines(InternalHologram hologram) {
        return CollectionUtils.toArrayList(hologram.getLines(), InternalHologramLine::getSerializedString);
    }

    public List<InternalHologramLine> deserializeLines() throws InternalHologramLoadException {
        List<String> serializedLines = configSection.getStringList("lines");

        if (serializedLines == null || serializedLines.size() == 0) {
            throw new InternalHologramLoadException("at least one line is required");
        }

        List<InternalHologramLine> lines = new ArrayList<>();
        for (String serializedLine : serializedLines) {
            try {
                lines.add(InternalHologramLineParser.parseLine(serializedLine));
            } catch (InternalHologramLoadException e) {
                // Rethrow with more details
                throw new InternalHologramLoadException("invalid line: " + e.getMessage(), e);
            }
        }
        return lines;
    }

    private ConfigSection serializePosition(Position position) {
        ConfigSection positionConfigSection = new ConfigSection();
        positionConfigSection.setString("world", position.getWorldName());
        positionConfigSection.setDouble("x", position.getX());
        positionConfigSection.setDouble("y", position.getY());
        positionConfigSection.setDouble("z", position.getZ());
        return positionConfigSection;
    }

    public Position deserializePosition() throws InternalHologramLoadException {
        ConfigSection positionConfigSection = configSection.getConfigSection("position");

        if (positionConfigSection == null) {
            throw new InternalHologramLoadException("no position set");
        }

        try {
            String worldName = positionConfigSection.getRequiredString("world");
            double x = positionConfigSection.getRequiredDouble("x");
            double y = positionConfigSection.getRequiredDouble("y");
            double z = positionConfigSection.getRequiredDouble("z");
            return Position.of(worldName, x, y, z);
        } catch (ConfigValueException e) {
            throw new InternalHologramLoadException("invalid position attribute \"" + e.getConfigPath() + "\"", e);
        }
    }

}
