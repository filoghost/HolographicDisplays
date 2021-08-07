/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.ConfigSection;
import me.filoghost.fcommons.config.ConfigType;
import me.filoghost.fcommons.config.ConfigValue;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.plugin.config.ConfigManager;

import java.nio.file.Path;

public class DatabaseLegacyUpgrade extends LegacyUpgrade implements LegacyUpgradeTask {

    private final ConfigLoader databaseConfigLoader;

    public DatabaseLegacyUpgrade(ConfigManager configManager, ErrorCollector errorCollector) {
        super(configManager, errorCollector);
        this.databaseConfigLoader = configManager.getConfigLoader("database.yml");
    }

    @Override
    public Path getFile() {
        return databaseConfigLoader.getFile();
    }

    @Override
    public void run() throws ConfigException {
        if (!databaseConfigLoader.fileExists()) {
            return; // Database file doesn't exist, nothing to upgrade
        }

        FileConfig databaseConfig = databaseConfigLoader.load();
        boolean changed = false;

        for (ConfigSection hologramSection : databaseConfig.toMap(ConfigType.SECTION).values()) {
            if (!hologramSection.contains("position")) {
                String legacySerializedLocation = hologramSection.getString("location");
                if (legacySerializedLocation != null) {
                    hologramSection.remove("location");
                    hologramSection.setConfigSection("position", convertLegacySerializedLocation(legacySerializedLocation));
                    changed = true;
                }
            }
        }

        if (changed) {
            createBackupFile(databaseConfigLoader.getFile());
            databaseConfigLoader.save(databaseConfig);
        }
    }

    private ConfigSection convertLegacySerializedLocation(String legacySerializedLocation) {
        String[] legacyLocationParts = Strings.splitAndTrim(legacySerializedLocation, ",");

        ConfigSection positionSection = new ConfigSection();
        positionSection.setString("world", legacyLocationParts[0]);
        if (legacyLocationParts.length > 1) {
            positionSection.set("x", getDoubleOrString(legacyLocationParts[1]));
        }
        if (legacyLocationParts.length > 2) {
            positionSection.set("y", getDoubleOrString(legacyLocationParts[2]));
        }
        if (legacyLocationParts.length > 3) {
            positionSection.set("z", getDoubleOrString(legacyLocationParts[3]));
        }
        return positionSection;
    }

    private static ConfigValue getDoubleOrString(String serializedDouble) {
        try {
            return ConfigValue.of(ConfigType.DOUBLE, Double.parseDouble(serializedDouble));
        } catch (NumberFormatException e) {
            return ConfigValue.of(ConfigType.STRING, serializedDouble);
        }
    }

}
