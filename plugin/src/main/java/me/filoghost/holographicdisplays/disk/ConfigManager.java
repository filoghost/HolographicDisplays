/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.disk;

import me.filoghost.fcommons.config.BaseConfigManager;
import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.ConfigLoader;
import me.filoghost.fcommons.config.FileConfig;
import me.filoghost.fcommons.config.exception.ConfigException;
import me.filoghost.fcommons.config.mapped.MappedConfigLoader;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.object.internal.InternalHologramManager;

import java.nio.file.Path;

public class ConfigManager extends BaseConfigManager {

    private final MappedConfigLoader<MainConfigModel> mainConfigLoader;
    private final ConfigLoader databaseConfigLoader;
    private final ConfigLoader placeholdersConfigLoader;

    public ConfigManager(Path rootDataFolder) {
        super(rootDataFolder);
        this.mainConfigLoader = getMappedConfigLoader("config.yml", MainConfigModel.class);
        this.databaseConfigLoader = getConfigLoader("database.yml");
        this.placeholdersConfigLoader = getConfigLoader("custom-placeholders.yml");
    }

    public void reloadMainConfig() {
        MainConfigModel mainConfig;

        try {
            mainConfig = mainConfigLoader.init();
        } catch (ConfigException e) {
            logConfigInitException(mainConfigLoader.getFile(), e);
            mainConfig = new MainConfigModel(); // Fallback: use default values
        }
        
        Configuration.load(mainConfig);
    }

    public HologramDatabase loadHologramDatabase() {
        Config databaseConfig;

        try {
            databaseConfig = databaseConfigLoader.init();
        } catch (ConfigException e) {
            logConfigInitException(databaseConfigLoader.getFile(), e);
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
            logConfigSaveException(databaseConfigLoader.getFile(), e);
        }
    }

    public void reloadCustomPlaceholders() {
        FileConfig placeholdersConfig;
        
        try {
            placeholdersConfig = placeholdersConfigLoader.init();
        } catch (ConfigException e) {
            logConfigInitException(placeholdersConfigLoader.getFile(), e);
            placeholdersConfig = new FileConfig(placeholdersConfigLoader.getFile()); // Fallback: empty config
        }
        
        CustomPlaceholders.load(placeholdersConfig);
    }

    public Path getAnimationsFolder() {
        return getRootDataFolder().resolve("animations");
    }
    
    public ConfigLoader getExampleAnimationLoader() {
        return getConfigLoader(getAnimationsFolder().resolve("example.txt"));
    }

    private void logConfigInitException(Path file, ConfigException e) {
        Log.severe("error while initializing config file \"" + formatPath(file) + "\"", e);
    }

    private void logConfigSaveException(Path file, ConfigException e) {
        Log.severe("error while saving config file \"" + formatPath(file) + "\"", e);
    }

    private String formatPath(Path path) {
       return ConfigErrors.formatPath(getRootDataFolder(), path);
    }

}
