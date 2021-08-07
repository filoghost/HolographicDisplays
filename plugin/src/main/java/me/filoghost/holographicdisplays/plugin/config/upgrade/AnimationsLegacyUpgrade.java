/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.config.exception.ConfigSaveException;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.plugin.config.ConfigManager;
import me.filoghost.holographicdisplays.plugin.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class AnimationsLegacyUpgrade extends LegacyUpgrade {

    private static final String SPEED_PREFIX = "speed:";

    private final Path animationFolder;

    public AnimationsLegacyUpgrade(ConfigManager configManager, ErrorCollector errorCollector) {
        super(configManager, errorCollector);
        this.animationFolder = configManager.getAnimationsFolder();
    }

    @Override
    public Path getFile() {
        return animationFolder;
    }

    @Override
    public void run() throws IOException {
        if (!Files.isDirectory(animationFolder)) {
            return;
        }

        try (Stream<Path> animationFiles = Files.list(animationFolder)) {
            animationFiles.filter(Files::isRegularFile).forEach(file -> {
                tryRun(file, () -> upgradeAnimationFile(file));
            });
        }
    }


    private void upgradeAnimationFile(Path oldFile) throws IOException, ConfigSaveException {
        String oldFileName = oldFile.getFileName().toString();
        if (FileUtils.hasFileExtension(oldFileName, "yml")) {
            return; // Probably a file already with the new format
        }

        List<String> lines = Files.readAllLines(oldFile);
        if (lines.size() == 0) {
            return;
        }

        // Remove the first line that only contains the speed
        String firstLine = lines.remove(0).trim();
        if (!firstLine.toLowerCase().startsWith(SPEED_PREFIX)) {
            return; // Not a valid animation
        }

        String newFileName;
        if (FileUtils.hasFileExtension(oldFileName, "txt")) {
            newFileName = FileUtils.removeFileExtension(oldFileName) + ".yml";
        } else {
            newFileName = oldFileName + ".yml";
        }
        Path newFile = oldFile.resolveSibling(newFileName);

        if (Files.isRegularFile(newFile)) {
            return; // Already existing, do not override
        }

        double speed;
        try {
            speed = Double.parseDouble(firstLine.substring(SPEED_PREFIX.length()).trim());
        } catch (NumberFormatException e) {
            speed = 0.5;
        }

        Config config = new Config();
        config.setDouble("interval-seconds", speed);
        config.setStringList("animation-frames", lines);

        createBackupFile(oldFile);
        configManager.getConfigLoader(newFile).save(config);
        Files.delete(oldFile);
    }

}
