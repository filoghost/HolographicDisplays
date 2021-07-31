/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config;

import me.filoghost.fcommons.config.BaseConfigManager;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.config.mapped.MappedConfigLoader;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;

import java.nio.file.Path;

public class ConfigManager extends BaseConfigManager {

    private final MappedConfigLoader<SettingsModel> mainConfigLoader;
    private final ConfigLoader databaseConfigLoader;
    private final ConfigLoader staticReplacementsConfigLoader;

    public ConfigManager(Path rootDataFolder) {
        super(rootDataFolder);
        this.mainConfigLoader = getMappedConfigLoader("config.yml", SettingsModel.class);
        this.databaseConfigLoader = getConfigLoader("database.yml");
        this.staticReplacementsConfigLoader = getConfigLoader("custom-placeholders.yml");
    }

    public void reloadMainSettings(ErrorCollector errorCollector) {
        SettingsModel mainConfig;

        try {
            mainConfig = mainConfigLoader.init();
        } catch (ConfigException e) {
            logConfigInitException(errorCollector, mainConfigLoader.getFile(), e);
            mainConfig = new SettingsModel(); // Fallback: use default values
        }

        Settings.load(mainConfig, errorCollector);
    }

    public HologramDatabase loadHologramDatabase(ErrorCollector errorCollector) {
        Config databaseConfig;

        try {
            databaseConfig = databaseConfigLoader.init();
        } catch (ConfigException e) {
            logConfigInitException(errorCollector, databaseConfigLoader.getFile(), e);
            databaseConfig = new Config(); // Fallback: empty config
        }

        HologramDatabase hologramDatabase = new HologramDatabase();
        hologramDatabase.loadFromConfig(databaseConfig);
        return hologramDatabase;
    }

    public void saveHologramDatabase(InternalHologramManager hologramManager) {
        try {
            databaseConfigLoader.save(HologramDatabase.exportToConfig(hologramManager));
        } catch (ConfigException e) {
            Log.severe("Error while saving holograms database file \"" + formatPath(databaseConfigLoader.getFile()) + "\"", e);
        }
    }

    public void reloadStaticReplacements(ErrorCollector errorCollector) {
        FileConfig staticReplacementsConfig;

        try {
            staticReplacementsConfig = staticReplacementsConfigLoader.init();
        } catch (ConfigException e) {
            logConfigInitException(errorCollector, staticReplacementsConfigLoader.getFile(), e);
            staticReplacementsConfig = new FileConfig(staticReplacementsConfigLoader.getFile()); // Fallback: empty config
        }

        StaticReplacements.load(staticReplacementsConfig, errorCollector);
    }

    public Path getAnimationsFolder() {
        return getRootDataFolder().resolve("animations");
    }

    public ConfigLoader getExampleAnimationLoader() {
        return getConfigLoader(getAnimationsFolder().resolve("example.txt"));
    }

    private void logConfigInitException(ErrorCollector errorCollector, Path file, ConfigException e) {
        errorCollector.add(e, "error while initializing config file \"" + formatPath(file) + "\"");
    }

    private String formatPath(Path path) {
        return ConfigErrors.formatPath(getRootDataFolder(), path);
    }

}
