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
import me.filoghost.fcommons.config.exception.ConfigLoadException;
import me.filoghost.fcommons.config.exception.ConfigSaveException;
import me.filoghost.fcommons.config.exception.ConfigValueException;
import me.filoghost.fcommons.config.mapped.MappedConfigLoader;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.plugin.hologram.internal.InternalHologramManager;
import me.filoghost.holographicdisplays.plugin.placeholder.internal.AnimationPlaceholder;
import me.filoghost.holographicdisplays.plugin.placeholder.internal.AnimationPlaceholderFactory;
import me.filoghost.holographicdisplays.plugin.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
            logConfigException(errorCollector, mainConfigLoader.getFile(), e);
            mainConfig = new SettingsModel(); // Fallback: use default values
        }

        Settings.load(mainConfig, errorCollector);
    }

    public HologramDatabase loadHologramDatabase(ErrorCollector errorCollector) {
        Config databaseConfig;

        try {
            databaseConfig = databaseConfigLoader.init();
        } catch (ConfigException e) {
            logConfigException(errorCollector, databaseConfigLoader.getFile(), e);
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

    public AnimationPlaceholderFactory loadAnimations(ErrorCollector errorCollector) {
        Map<String, AnimationPlaceholder> animationsByFileName = new HashMap<>();
        Path animationsFolder = getAnimationsFolder();

        try {
            if (!Files.isDirectory(animationsFolder)) {
                Files.createDirectories(animationsFolder);
                try {
                    Path exampleAnimationFile = animationsFolder.resolve("example.yml");
                    getConfigLoader(exampleAnimationFile).createDefault();
                } catch (ConfigSaveException e) {
                    errorCollector.add(e, "could not add example animation file");
                }
            }

            try (Stream<Path> animationFiles = Files.list(animationsFolder)) {
                animationFiles.filter(this::isYamlFile).forEach(file -> {
                    try {
                        String fileName = file.getFileName().toString();
                        AnimationConfig animationConfig = loadAnimation(file);
                        AnimationPlaceholder animationPlaceholder = new AnimationPlaceholder(
                                animationConfig.getIntervalTicks(),
                                animationConfig.getFrames());
                        animationsByFileName.put(fileName, animationPlaceholder);
                    } catch (ConfigException e) {
                        logConfigException(errorCollector, file, e);
                    }
                });
            }
        } catch (IOException e) {
            errorCollector.add(e, "error loading animation files");
        }

        return new AnimationPlaceholderFactory(animationsByFileName);
    }

    private AnimationConfig loadAnimation(Path animationFile) throws ConfigLoadException, ConfigValueException {
        FileConfig animationFileConfig = getConfigLoader(animationFile).load();
        return new AnimationConfig(animationFileConfig);
    }

    public void reloadStaticReplacements(ErrorCollector errorCollector) {
        FileConfig staticReplacementsConfig;

        try {
            staticReplacementsConfig = staticReplacementsConfigLoader.init();
        } catch (ConfigException e) {
            logConfigException(errorCollector, staticReplacementsConfigLoader.getFile(), e);
            staticReplacementsConfig = new FileConfig(staticReplacementsConfigLoader.getFile()); // Fallback: empty config
        }

        StaticReplacements.load(staticReplacementsConfig, errorCollector);
    }

    public Path getAnimationsFolder() {
        return getRootDataFolder().resolve("animations");
    }

    private boolean isYamlFile(Path file) {
        if (!Files.isRegularFile(file)) {
            return false;
        }

        return FileUtils.hasFileExtension(file, "yml", "yaml");
    }

    private void logConfigException(ErrorCollector errorCollector, Path file, ConfigException e) {
        errorCollector.add(e, "error while loading config file \"" + formatPath(file) + "\"");
    }

    public String formatPath(Path path) {
        return ConfigErrors.formatPath(getRootDataFolder(), path);
    }

}
