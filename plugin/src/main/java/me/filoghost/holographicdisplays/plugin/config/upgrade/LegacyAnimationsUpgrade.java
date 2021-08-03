/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.plugin.config.upgrade;

import me.filoghost.fcommons.config.Config;
import me.filoghost.fcommons.logging.ErrorCollector;
import me.filoghost.holographicdisplays.plugin.config.ConfigManager;
import me.filoghost.holographicdisplays.plugin.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class LegacyAnimationsUpgrade {

    private static final String SPEED_PREFIX = "speed:";

    public static void run(ConfigManager configManager, ErrorCollector errorCollector) throws IOException {
        Path animationFolder = configManager.getAnimationsFolder();

        if (!Files.isDirectory(animationFolder)) {
            return;
        }

        try (Stream<Path> animationFiles = Files.list(animationFolder)) {
            animationFiles.filter(Files::isRegularFile).forEach(file -> convertFile(file, configManager, errorCollector));
        }
    }

    private static void convertFile(Path oldFile, ConfigManager configManager, ErrorCollector errorCollector) {
        if (LegacyUpgradeUtils.isBackupFile(oldFile)) {
            return; // Ignore backup files
        }

        try {
            List<String> lines = Files.readAllLines(oldFile);
            if (lines.size() == 0) {
                return;
            }

            // Remove the first line that only contains the speed
            String firstLine = lines.remove(0).trim();
            if (!firstLine.toLowerCase().startsWith(SPEED_PREFIX)) {
                return; // Not a valid animation
            }

            String newFileName = oldFile.getFileName().toString();
            if (FileUtils.hasFileExtension(newFileName, "txt")) {
                newFileName = FileUtils.removeFileExtension(newFileName);
            }
            newFileName += ".yml";
            Path newFile = oldFile.resolveSibling(newFileName);

            if (Files.isRegularFile(newFile)) {
                return; // Already created, do not override
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
            configManager.getConfigLoader(newFile).save(config);

            Files.move(oldFile, LegacyUpgradeUtils.getBackupFile(oldFile));
        } catch (Exception e) {
            errorCollector.add(e, "couldn't automatically convert animation file \"" + oldFile.getFileName() + "\" to the new format");
        }
    }

}
