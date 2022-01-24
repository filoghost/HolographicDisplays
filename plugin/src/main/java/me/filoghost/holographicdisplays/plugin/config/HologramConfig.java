/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.exception.ConfigValueException;
import me.filoghost.holographicdisplays.plugin.hologram.base.ImmutablePosition;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologram;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramLine;
import me.filoghost.holographicdisplays.plugin.internal.hologram.InternalHologramManager;

import java.util.ArrayList;
import java.util.List;

public class HologramConfig {

    private final String name;
    private final List<String> serializedLines;
    private final ConfigSection positionConfigSection;
    private final int viewRange;

    public HologramConfig(String name, ConfigSection configSection) {
        this.name = name;
        this.serializedLines = configSection.getStringList("lines");
        this.positionConfigSection = configSection.getConfigSection("position");
        this.viewRange = configSection.getInt("viewRange", -1);
    }

    public HologramConfig(InternalHologram hologram) {
        this.name = hologram.getName();
        this.serializedLines = new ArrayList<>();
        for (InternalHologramLine line : hologram.getLines()) {
            serializedLines.add(line.getSerializedConfigValue());
        }

        ImmutablePosition position = hologram.getPosition();
        this.positionConfigSection = new ConfigSection();
        positionConfigSection.setString("world", position.getWorldName());
        positionConfigSection.setDouble("x", position.getX());
        positionConfigSection.setDouble("y", position.getY());
        positionConfigSection.setDouble("z", position.getZ());
        viewRange = hologram.getViewRange();
    }

    public ConfigSection toConfigSection() {
        ConfigSection configSection = new ConfigSection();
        configSection.setStringList("lines", serializedLines);
        configSection.setConfigSection("position", positionConfigSection);
        configSection.setInt("viewRange", viewRange);
        return configSection;
    }

    public void createHologram(InternalHologramManager internalHologramManager) throws HologramLoadException {
        if (serializedLines == null || serializedLines.size() == 0) {
            throw new HologramLoadException("at least one line is required");
        }
        if (positionConfigSection == null) {
            throw new HologramLoadException("no position set");
        }

        ImmutablePosition position = parsePosition();
        InternalHologram hologram = internalHologramManager.createHologram(position, name, viewRange);
        List<InternalHologramLine> lines = new ArrayList<>();

        for (String serializedLine : serializedLines) {
            try {
                lines.add(HologramLineParser.parseLine(hologram, serializedLine));
            } catch (HologramLoadException e) {
                // Rethrow with more details
                throw new HologramLoadException("invalid line: " + e.getMessage(), e);
            }
        }

        hologram.getLines().addAll(lines);
    }

    private ImmutablePosition parsePosition() throws HologramLoadException {
        try {
            String worldName = positionConfigSection.getRequiredString("world");
            double x = positionConfigSection.getRequiredDouble("x");
            double y = positionConfigSection.getRequiredDouble("y");
            double z = positionConfigSection.getRequiredDouble("z");
            return new ImmutablePosition(worldName, x, y, z);
        } catch (ConfigValueException e) {
            throw new HologramLoadException("invalid position attribute \"" + e.getConfigPath() + "\"", e);
        }
    }

    public String getName() {
        return name;
    }

}
